const env = {
  appName: "OmniMind",
  appSlug: "omnimind",
  scheme: "omnimind",
  iosBundleId: "com.omnimind.app",
  androidPackage: "com.omnimind.app",
};

const config = {
  name: env.appName,
  slug: env.appSlug,
  version: "2.1.0",
  orientation: "portrait",
  icon: "./assets/images/icon.png",
  scheme: env.scheme,
  userInterfaceStyle: "dark",
  newArchEnabled: true,
  splash: {
    backgroundColor: "#0A0A0F",
    image: "./assets/images/splash-icon.png",
    resizeMode: "contain",
    imageWidth: 200,
  },
  ios: {
    supportsTablet: true,
    bundleIdentifier: env.iosBundleId,
    infoPlist: {
      ITSAppUsesNonExemptEncryption: false,
    },
  },
  android: {
    adaptiveIcon: {
      backgroundColor: "#0A0A0F",
      foregroundImage: "./assets/images/android-icon-foreground.png",
      backgroundImage: "./assets/images/android-icon-background.png",
      monochromeImage: "./assets/images/android-icon-monochrome.png",
    },
    edgeToEdgeEnabled: true,
    package: env.androidPackage,
    permissions: ["POST_NOTIFICATIONS"],
  },
  web: {
    bundler: "metro",
    output: "static",
    favicon: "./assets/images/favicon.png",
  },
  plugins: [
    "expo-router",
    "expo-font",
    [
      "expo-splash-screen",
      {
        image: "./assets/images/splash-icon.png",
        imageWidth: 200,
        resizeMode: "contain",
        backgroundColor: "#0A0A0F",
        dark: {
          backgroundColor: "#0A0A0F",
        },
      },
    ],
    [
      "expo-build-properties",
      {
        android: {
          buildArchs: ["arm64-v8a", "armeabi-v7a"],
          minSdkVersion: 24,
          compileSdkVersion: 35,
          targetSdkVersion: 34,
          enableProguardInReleaseBuilds: false,
          kotlinVersion: "1.9.24",
        },
        ios: {
          deploymentTarget: "15.1",
        },
      },
    ],
  ],
  experiments: {
    typedRoutes: true,
  },
  extra: {
    eas: {
      projectId: "01bf0e7e-9143-4e6c-bbbb-fe47bec3b4ad",
    },
  },
};

module.exports = config;
