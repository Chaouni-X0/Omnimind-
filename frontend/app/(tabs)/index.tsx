import React, { useState, useCallback } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  ScrollView,
  Image,
  Dimensions,
  StyleSheet,
  KeyboardAvoidingView,
  Platform,
} from "react-native";
import { useColors } from "../../hooks/useTheme";

const { width } = Dimensions.get("window");

interface ActionItem {
  id: string;
  title: string;
  subtitle: string;
  icon: string;
  color: string;
}

const quickActions: ActionItem[] = [
  {
    id: "1",
    title: "إنشاء تطبيق",
    subtitle: "بنِ تطبيق موبايل كامل",
    icon: "📱",
    color: "#7C5CFC",
  },
  {
    id: "2",
    title: "إنشاء لعبة",
    subtitle: "صمّم لعبة ممتعة",
    icon: "🎮",
    color: "#FF6B8A",
  },
  {
    id: "3",
    title: "رفع ملف",
    subtitle: "أضف صورة أو ملف",
    icon: "📁",
    color: "#00E5CC",
  },
  {
    id: "4",
    title: "كتابة كود",
    subtitle: "محرر الكود الذكي",
    icon: "💻",
    color: "#FFB020",
  },
];

const recentItems = [
  { id: "1", name: "تطبيق المهام", time: "قبل ساعتين", type: "app" },
  { id: "2", name: "لعبة المغامرة", time: "أمس", type: "game" },
  { id: "3", name: "موقع بورتفوليو", time: "قبل 3 أيام", type: "web" },
];

export default function HomeScreen() {
  const colors = useColors();
  const [message, setMessage] = useState("");
  const [isFocused, setIsFocused] = useState(false);

  const styles = StyleSheet.create({
    container: {
      flex: 1,
      backgroundColor: colors.background,
    },
    content: {
      flex: 1,
    },
    header: {
      paddingHorizontal: 24,
      paddingTop: 60,
      paddingBottom: 32,
    },
    logoContainer: {
      alignItems: "center",
      marginBottom: 8,
    },
    logo: {
      width: 64,
      height: 64,
      borderRadius: 16,
    },
    title: {
      fontSize: 28,
      fontWeight: "800",
      color: colors.foreground,
      textAlign: "center",
      letterSpacing: -0.5,
    },
    subtitle: {
      fontSize: 14,
      color: colors.muted,
      textAlign: "center",
      marginTop: 6,
      lineHeight: 20,
    },
    inputContainer: {
      marginHorizontal: 20,
      marginTop: 8,
    },
    inputWrapper: {
      backgroundColor: colors.surface,
      borderRadius: 20,
      borderWidth: 1,
      borderColor: isFocused ? colors.primary : colors.border,
      paddingHorizontal: 20,
      paddingVertical: 16,
      shadowColor: "#000",
      shadowOffset: { width: 0, height: 4 },
      shadowOpacity: 0.2,
      shadowRadius: 12,
      elevation: 8,
    },
    input: {
      fontSize: 16,
      color: colors.foreground,
      minHeight: 60,
      textAlignVertical: "top",
      lineHeight: 24,
    },
    inputPlaceholder: {
      color: colors.muted,
      fontSize: 16,
    },
    bottomBar: {
      flexDirection: "row",
      justifyContent: "space-between",
      alignItems: "center",
      marginTop: 12,
      paddingHorizontal: 4,
    },
    attachBtn: {
      flexDirection: "row",
      alignItems: "center",
      gap: 6,
      paddingVertical: 8,
      paddingHorizontal: 12,
      borderRadius: 12,
      backgroundColor: colors.surfaceSecondary,
    },
    attachText: {
      fontSize: 13,
      color: colors.muted,
      fontWeight: "500",
    },
    sendBtn: {
      width: 44,
      height: 44,
      borderRadius: 22,
      backgroundColor: colors.primary,
      justifyContent: "center",
      alignItems: "center",
      shadowColor: colors.primary,
      shadowOffset: { width: 0, height: 4 },
      shadowOpacity: 0.4,
      shadowRadius: 8,
      elevation: 6,
    },
    sendText: {
      color: "#0A0A0F",
      fontSize: 20,
      fontWeight: "700",
    },
    actionsSection: {
      paddingHorizontal: 20,
      marginTop: 32,
    },
    actionsTitle: {
      fontSize: 16,
      fontWeight: "700",
      color: colors.foreground,
      marginBottom: 16,
    },
    actionsGrid: {
      flexDirection: "row",
      flexWrap: "wrap",
      gap: 12,
    },
    actionCard: {
      width: (width - 52) / 2,
      backgroundColor: colors.surface,
      borderRadius: 16,
      padding: 16,
      borderWidth: 1,
      borderColor: colors.border,
    },
    actionIcon: {
      width: 40,
      height: 40,
      borderRadius: 12,
      justifyContent: "center",
      alignItems: "center",
      marginBottom: 12,
    },
    actionIconText: {
      fontSize: 20,
    },
    actionTitle: {
      fontSize: 14,
      fontWeight: "700",
      color: colors.foreground,
      marginBottom: 4,
    },
    actionSubtitle: {
      fontSize: 12,
      color: colors.muted,
      lineHeight: 16,
    },
    recentSection: {
      paddingHorizontal: 20,
      marginTop: 32,
      marginBottom: 32,
    },
    recentTitle: {
      fontSize: 16,
      fontWeight: "700",
      color: colors.foreground,
      marginBottom: 16,
    },
    recentItem: {
      flexDirection: "row",
      alignItems: "center",
      justifyContent: "space-between",
      paddingVertical: 14,
      borderBottomWidth: 1,
      borderBottomColor: colors.borderLight,
    },
    recentItemInfo: {
      flex: 1,
    },
    recentName: {
      fontSize: 14,
      fontWeight: "600",
      color: colors.foreground,
      marginBottom: 2,
    },
    recentTime: {
      fontSize: 12,
      color: colors.mutedLight,
    },
    recentType: {
      fontSize: 11,
      color: colors.primary,
      fontWeight: "600",
      backgroundColor: colors.primary + "15",
      paddingHorizontal: 10,
      paddingVertical: 4,
      borderRadius: 8,
      textTransform: "uppercase",
    },
    glowLine: {
      height: 2,
      backgroundColor: colors.primary,
      opacity: 0.3,
      marginHorizontal: 20,
      borderRadius: 1,
    },
  });

  const handleSend = useCallback(() => {
    if (message.trim()) {
      // Placeholder - will connect to AI later
      setMessage("");
    }
  }, [message]);

  return (
    <KeyboardAvoidingView
      style={styles.container}
      behavior={Platform.OS === "ios" ? "padding" : undefined}
      keyboardVerticalOffset={Platform.OS === "ios" ? 0 : 24}
    >
      <ScrollView
        contentContainerStyle={styles.content}
        showsVerticalScrollIndicator={false}
        keyboardShouldPersistTaps="handled"
      >
        {/* Header with Logo */}
        <View style={styles.header}>
          <View style={styles.logoContainer}>
            <Image
              source={require("../../assets/images/icon.png")}
              style={styles.logo}
            />
          </View>
          <Text style={styles.title}>OmniMind</Text>
          <Text style={styles.subtitle}>
            بيئتك المتكاملة للتطوير الذكي
          </Text>
        </View>

        {/* Message Input */}
        <View style={styles.inputContainer}>
          <View style={styles.inputWrapper}>
            <TextInput
              style={styles.input}
              placeholder="اكتب رسالتك هنا..."
              placeholderTextColor={colors.muted}
              value={message}
              onChangeText={setMessage}
              onFocus={() => setIsFocused(true)}
              onBlur={() => setIsFocused(false)}
              multiline
              numberOfLines={4}
              textAlign="right"
            />
          </View>

          <View style={styles.bottomBar}>
            <TouchableOpacity style={styles.attachBtn} activeOpacity={0.7}>
              <Text style={styles.attachText}>📎 إرفاق ملف</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={styles.sendBtn}
              onPress={handleSend}
              activeOpacity={0.8}
            >
              <Text style={styles.sendText}>↑</Text>
            </TouchableOpacity>
          </View>
        </View>

        {/* Glow line separator */}
        <View style={styles.glowLine} />

        {/* Quick Actions */}
        <View style={styles.actionsSection}>
          <Text style={styles.actionsTitle}>اقتراحات سريعة</Text>
          <View style={styles.actionsGrid}>
            {quickActions.map((action) => (
              <TouchableOpacity
                key={action.id}
                style={styles.actionCard}
                activeOpacity={0.7}
              >
                <View
                  style={[
                    styles.actionIcon,
                    { backgroundColor: action.color + "20" },
                  ]}
                >
                  <Text style={styles.actionIconText}>{action.icon}</Text>
                </View>
                <Text style={styles.actionTitle}>{action.title}</Text>
                <Text style={styles.actionSubtitle}>{action.subtitle}</Text>
              </TouchableOpacity>
            ))}
          </View>
        </View>

        {/* Recent Projects */}
        <View style={styles.recentSection}>
          <Text style={styles.recentTitle}>آخر المشاريع</Text>
          {recentItems.map((item) => (
            <TouchableOpacity key={item.id} style={styles.recentItem} activeOpacity={0.7}>
              <View style={styles.recentItemInfo}>
                <Text style={styles.recentName}>{item.name}</Text>
                <Text style={styles.recentTime}>{item.time}</Text>
              </View>
              <Text style={styles.recentType}>{item.type}</Text>
            </TouchableOpacity>
          ))}
        </View>
      </ScrollView>
    </KeyboardAvoidingView>
  );
}
