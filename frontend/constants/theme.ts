import { themeColors } from '../theme.config';

export type ColorScheme = 'light' | 'dark';
export type ThemeName = 'default' | 'minimal' | 'vibrant';

export type ThemeColorPalette = typeof Colors.light;

export const Colors = {
  light: {
    primary: themeColors.primary.light,
    background: themeColors.background.light,
    surface: themeColors.surface.light,
    surfaceSecondary: themeColors.surfaceSecondary.light,
    foreground: themeColors.foreground.light,
    muted: themeColors.muted.light,
    mutedLight: themeColors.mutedLight.light,
    border: themeColors.border.light,
    borderLight: themeColors.borderLight.light,
    success: themeColors.success.light,
    warning: themeColors.warning.light,
    error: themeColors.error.light,
    info: themeColors.info.light,
    accent: themeColors.accent.light,
    accentSecondary: themeColors.accentSecondary.light,
    glass: themeColors.glass.light,
    glassLight: themeColors.glassLight.light,
  },
  dark: {
    primary: themeColors.primary.dark,
    background: themeColors.background.dark,
    surface: themeColors.surface.dark,
    surfaceSecondary: themeColors.surfaceSecondary.dark,
    foreground: themeColors.foreground.dark,
    muted: themeColors.muted.dark,
    mutedLight: themeColors.mutedLight.dark,
    border: themeColors.border.dark,
    borderLight: themeColors.borderLight.dark,
    success: themeColors.success.dark,
    warning: themeColors.warning.dark,
    error: themeColors.error.dark,
    info: themeColors.info.dark,
    accent: themeColors.accent.dark,
    accentSecondary: themeColors.accentSecondary.dark,
    glass: themeColors.glass.dark,
    glassLight: themeColors.glassLight.dark,
  },
};

export const THEMES: Record<ThemeName, string> = {
  default: 'Modern Professional',
  minimal: 'Minimal Clean',
  vibrant: 'Vibrant Energy',
};

export const SPACING = {
  xs: 4,
  sm: 8,
  md: 12,
  lg: 16,
  xl: 24,
  '2xl': 32,
  '3xl': 48,
};

export const BORDER_RADIUS = {
  sm: 8,
  md: 12,
  lg: 16,
  xl: 20,
  '2xl': 24,
  '3xl': 32,
  full: 9999,
};

export const FONT_SIZES = {
  xs: 12,
  sm: 14,
  base: 16,
  lg: 18,
  xl: 20,
  '2xl': 24,
  '3xl': 30,
  '4xl': 36,
};

export const FONT_WEIGHTS = {
  light: '300',
  normal: '400',
  medium: '500',
  semibold: '600',
  bold: '700',
  extrabold: '800',
};
