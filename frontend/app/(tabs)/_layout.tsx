import { Tabs } from "expo-router";
import { useColors } from "../../hooks/useTheme";

export default function TabsLayout() {
  const colors = useColors();

  return (
    <Tabs
      screenOptions={{
        headerShown: false,
        tabBarActiveTintColor: colors.primary,
        tabBarInactiveTintColor: colors.muted,
        tabBarStyle: {
          backgroundColor: colors.surface,
          borderTopColor: colors.border,
          borderTopWidth: 1,
          paddingTop: 8,
          paddingBottom: 8,
          height: 64,
        },
        tabBarLabelStyle: {
          fontSize: 11,
          fontWeight: "600",
        },
      }}
    >
      <Tabs.Screen
        name="index"
        options={{
          title: "الرئيسية",
          tabBarIcon: ({ color, size }) => null,
        }}
      />
      <Tabs.Screen
        name="editor/page"
        options={{
          title: "المحرر",
          href: null,
        }}
      />
      <Tabs.Screen
        name="terminal/page"
        options={{
          title: "الترمنال",
          href: null,
        }}
      />
      <Tabs.Screen
        name="chat/page"
        options={{
          title: "الدردشة",
          href: null,
        }}
      />
      <Tabs.Screen
        name="github/page"
        options={{
          title: "GitHub",
          href: null,
        }}
      />
    </Tabs>
  );
}
