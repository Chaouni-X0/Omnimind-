import { Stack } from "expo-router";
import { StatusBar } from "expo-status-bar";
import { ThemeProvider } from "../lib/theme-provider";

export default function RootLayout() {
  return (
    <ThemeProvider>
      <StatusBar style="light" />
      <Stack
        screenOptions={{
          headerShown: false,
          contentStyle: { backgroundColor: "#0A0A0F" },
          animation: "fade",
        }}
      >
        <Stack.Screen name="(tabs)" />
      </Stack>
    </ThemeProvider>
  );
}
