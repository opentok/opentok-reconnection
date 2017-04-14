
#import "ViewController.h"

// Replace with your OpenTok API key
static NSString* const kApiKey = @"";
// Replace with your generated session ID
static NSString* const kSessionId = @"";
// Replace with your generated token
static NSString* const kToken = @"";

static double widgetHeight = 240;
static double widgetWidth = 320;

@interface ViewController ()
@property (nonatomic, strong) OTSession *session;
@property (nonatomic, strong) OTPublisher *publisher;
@property (nonatomic, strong) OTSubscriber *subscriber;
@property (nonatomic, strong) UIAlertController *alert;
@property (nonatomic, strong) UIView *streamReconnectingView;
@property (nonatomic, strong) UIActivityIndicatorView *reconnectingSpinner;

- (void)doConnect;
- (void)doPublish;
- (void)doSubscribe:(OTStream*)stream;

- (void)batteryLevelChanged:(NSNotification *)notification;
- (void)showReconnectingAlert;
- (void)dismissReconnectingAlert;
- (void)showErrorAlert:(NSString *)message;
- (void)showAlertWithMessage:(NSString *)message
                       title:(NSString *)title
          showDissmissButton:(BOOL)showButton;
@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [self buildReconnectingViews];
    
    self.session = [[OTSession alloc] initWithApiKey:kApiKey
                                           sessionId:kSessionId
                                            delegate:self];
    
    [UIDevice currentDevice].batteryMonitoringEnabled = YES;
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(batteryLevelChanged:)
                                                 name:UIDeviceBatteryLevelDidChangeNotification object:nil];

    [self doConnect];
}

#pragma mark - Opentok Lifecycle

- (void)doConnect
{
    OTError *error;
    
    [self.session connectWithToken:kToken error:&error];
    if (error) {
        [self showErrorAlert:[error localizedDescription]];
    }
}

- (void)doPublish
{
    OTPublisherSettings *settings = [[OTPublisherSettings alloc] init];
    settings.name = [UIDevice currentDevice].name;
    self.publisher = [[OTPublisher alloc] initWithDelegate:self settings:settings];
    
    OTError *error = nil;
    [self.session publish:self.publisher error:&error];
    if (error) {
        [self showErrorAlert:[error localizedDescription]];
    }
    
    [self.view addSubview:self.publisher.view];
    [self.publisher.view setFrame:CGRectMake(0, 0, widgetWidth, widgetHeight)];
}

- (void)doSubscribe:(OTStream*)stream
{
    self.subscriber = [[OTSubscriber alloc] initWithStream:stream delegate:self];
    
    OTError *error = nil;
    [self.session subscribe:self.subscriber error:&error];
    if (error) {
        [self showErrorAlert:[error localizedDescription]];
    }
}

#pragma mark - Session Delegate
- (void)sessionDidConnect:(OTSession *)session
{
    NSLog(@"sessionDidConnect (%@)", session.sessionId);

    [self doPublish];
}

- (void)session:(OTSession *)session didFailWithError:(OTError *)error
{
    NSLog(@"didFailWithError: (%@)", error);
    [self showErrorAlert:[error localizedDescription]];
}

- (void)session:(OTSession*)session streamCreated:(OTStream *)stream
{
    NSLog(@"session streamCreated (%@)", stream.streamId);

    if (!self.subscriber) {
        [self doSubscribe:stream];
    }
}

- (void)session:(OTSession *)session streamDestroyed:(OTStream *)stream
{
    if (self.subscriber) {
        [self.subscriber.view removeFromSuperview];
    }
}

- (void)sessionDidDisconnect:(OTSession *)session
{
    NSLog(@"session disconnected");
    if (self.publisher) {
        [self.publisher.view removeFromSuperview];
    }
    
    if (self.subscriber) {
        [self.subscriber.view removeFromSuperview];
    }
}

- (void)sessionDidBeginReconnecting:(OTSession *)session
{
    [self showReconnectingAlert];
}

- (void)sessionDidReconnect:(OTSession *)session
{
    [self dismissReconnectingAlert];
}

#pragma mark - Publisher Delegate

#pragma mark - Subscriber Delegate
- (void)subscriberDidConnectToStream:(OTSubscriberKit *)subscriber
{
    NSLog(@"subscriberDidConnectToStream (%@)", subscriber.stream.connection.connectionId);
    if (subscriber == self.subscriber) {
        [self.subscriber.view setFrame:CGRectMake(0, widgetHeight, widgetWidth,
                                                  widgetHeight)];
        [self.view addSubview:self.subscriber.view];
    }
}

- (void)subscriberDidDisconnectFromStream:(OTSubscriberKit *)subscriber
{
    [self.reconnectingSpinner startAnimating];
    [self.subscriber.view addSubview:self.streamReconnectingView];
}

- (void)subscriberDidReconnectToStream:(OTSubscriberKit *)subscriber
{
    [self.reconnectingSpinner stopAnimating];
    [self.streamReconnectingView removeFromSuperview];
}

#pragma mark - Utils
- (void)batteryLevelChanged:(NSNotification *)notification
{
    NSString *signalText = [NSString stringWithFormat:@"%f", [UIDevice currentDevice].batteryLevel];
    OTError *error;
    [self.session signalWithType:@"signal"
                          string:signalText
                      connection:nil
             retryAfterReconnect:NO
                           error:&error];
    
    if (error) {
        [self showErrorAlert:[error localizedDescription]];
    }
}

- (void)buildReconnectingViews
{
    self.streamReconnectingView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, widgetWidth, widgetHeight)];
    self.streamReconnectingView.backgroundColor = [UIColor blackColor];
    self.reconnectingSpinner = [[UIActivityIndicatorView alloc]
                                initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhite];
    self.reconnectingSpinner.center = CGPointMake(widgetWidth / 2, widgetHeight / 2);
    [self.streamReconnectingView addSubview:self.reconnectingSpinner];
}

- (void)showErrorAlert:(NSString *)message
{
    [self showAlertWithMessage:message
                         title:@"OTError"
            showDissmissButton:YES];
}

- (void)showReconnectingAlert
{
    [self showAlertWithMessage:@"Session is reconnecting"
                         title:@""
            showDissmissButton:NO];
}

- (void)dismissReconnectingAlert
{
    if (self.alert) {
        [self.alert dismissViewControllerAnimated:YES completion:nil];
    }
}

- (void)showAlertWithMessage:(NSString *)message
                       title:(NSString *)title
          showDissmissButton:(BOOL)showButton
{
    dispatch_async(dispatch_get_main_queue(), ^{
        self.alert = [UIAlertController alertControllerWithTitle:title
                                                    message:message
                                             preferredStyle:UIAlertControllerStyleAlert];
        if (showButton) {
            UIAlertAction *action = [UIAlertAction actionWithTitle:@"OK"
                                                             style:UIAlertActionStyleCancel
                                                           handler:^(UIAlertAction * _Nonnull action) {
                                                               [self.alert dismissViewControllerAnimated:YES completion:nil];
                                                           }];
            [self.alert addAction:action];

        }
        
        [self presentViewController:self.alert animated:YES completion:nil];
    });
}

@end
