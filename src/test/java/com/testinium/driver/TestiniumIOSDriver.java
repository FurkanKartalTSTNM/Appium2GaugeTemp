package com.testinium.driver;

import com.testinium.exception.ScreenRecordingException;
import com.testinium.util.Constants;
import com.testinium.util.TestiniumEnvironment;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.screenrecording.CanRecordScreen;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.UUID;

import static com.testinium.driver.TestiniumDriver.registerDriver;
import static com.testinium.util.Constants.CapabilityConstants.APPIUM_APP;
import static com.testinium.util.Constants.CapabilityConstants.APPIUM_AUTOMATION_NAME;
import static com.testinium.util.Constants.CapabilityConstants.APPIUM_AUTO_ACCEPT_ALERTS;
import static com.testinium.util.Constants.CapabilityConstants.APPIUM_BUNDLE_ID;
import static com.testinium.util.Constants.CapabilityConstants.DERIVED_DATA_PATH;
import static com.testinium.util.Constants.CapabilityConstants.NEW_COMMAND_TIMEOUT;
import static com.testinium.util.Constants.CapabilityConstants.UDID;
import static com.testinium.util.Constants.CapabilityConstants.USE_PREBUILD_WDA;
import static com.testinium.util.Constants.CapabilityConstants.WDA_CONNECTION_TIMEOUT;
import static com.testinium.util.Constants.CapabilityConstants.XCUI_TEST;
import static com.testinium.util.Constants.DEFAULT_PROFILE;
import static com.testinium.util.DeviceParkUtil.setDeviceParkOptions;
import static com.testinium.util.MediaUtil.startScreenRecordingForIOS;
import static com.testinium.util.MediaUtil.stopScreenRecordingForIOS;

@Slf4j
public class TestiniumIOSDriver extends IOSDriver implements CanRecordScreen {

    public TestiniumIOSDriver(URL hubUrl, DesiredCapabilities capabilities) throws Exception {
        super(new TestiniumCommandExecutor(hubUrl), overrideCapabilities(capabilities));
        registerDriver(this.getSessionId(), this);
        if (TestiniumEnvironment.isAllowedToTakeRecordVideo()){
            startScreenRecordingForIOS(this.getRemoteAddress(), String.valueOf(this.getSessionId()));
        }
        log.info("Driver initiated successfully");
    }

    private static DesiredCapabilities overrideCapabilities(DesiredCapabilities capabilities) {
        if (!DEFAULT_PROFILE.equals(TestiniumEnvironment.profile)) {
            return capabilities;
        }
        DesiredCapabilities overridden = new DesiredCapabilities(capabilities);
        overridden.setCapability(Constants.PLATFORM_NAME, Platform.IOS);
        overridden.setCapability(UDID, TestiniumEnvironment.udid);
        overridden.setCapability(APPIUM_AUTOMATION_NAME, XCUI_TEST);
        overridden.setCapability(APPIUM_BUNDLE_ID, TestiniumEnvironment.bundleId);
        overridden.setCapability(NEW_COMMAND_TIMEOUT, 300000);
        overridden.setCapability(WDA_CONNECTION_TIMEOUT, 600000);
        overridden.setCapability(APPIUM_APP, TestiniumEnvironment.app);
        overridden.setCapability(APPIUM_AUTO_ACCEPT_ALERTS, true);
        overridden.setCapability(USE_PREBUILD_WDA,true);
        overridden.setCapability(DERIVED_DATA_PATH, UUID.randomUUID().toString());

        setDeviceParkOptions(overridden);

        return overridden;
    }

    @Override
    public void quit() {
        try {
            if (TestiniumEnvironment.isAllowedToTakeRecordVideo()) {
                stopScreenRecordingForIOS(this.getRemoteAddress(), String.valueOf(this.getSessionId()));
            }
        } catch (Exception e) {
            log.error("Error occurred while recording screen for session {}", this.getSessionId(), e);
            throw new ScreenRecordingException(this.getSessionId().toString());
        }

        TestiniumDriver.postQuit(this);
        super.quit();
    }
}
