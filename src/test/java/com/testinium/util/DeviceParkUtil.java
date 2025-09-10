package com.testinium.util;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Optional;

import static com.testinium.util.Constants.CapabilityConstants.RESIGN_APP;
import static com.testinium.util.Constants.EnvironmentConstants.APPIUM_VERSION;
import static com.testinium.util.Constants.EnvironmentConstants.DP_OPTIONS;
import static com.testinium.util.Constants.EnvironmentConstants.SESSION_ID;
import static com.testinium.util.TestiniumEnvironment.bundleId;

public class DeviceParkUtil {

    public static void setDeviceParkOptions(DesiredCapabilities capabilities) {
        HashMap<String, Object> deviceParkOptions = new HashMap<>();
        deviceParkOptions.put(SESSION_ID, TestiniumEnvironment.sessionId);
        deviceParkOptions.put(APPIUM_VERSION, TestiniumEnvironment.appiumVersion);
        Optional.ofNullable(bundleId)
                .filter(id -> !id.isEmpty())
                .ifPresent(id -> deviceParkOptions.put(
                        RESIGN_APP, !"true".equalsIgnoreCase(TestiniumEnvironment.isSigned))
                );
        capabilities.setCapability(DP_OPTIONS, deviceParkOptions);
    }
}
