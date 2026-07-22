import { useThemeContext } from "../lib/theme-provider";

export function useColors() {
  const { colors } = useThemeContext();
  return colors;
}

export function useColorScheme() {
  const { colorScheme } = useThemeContext();
  return colorScheme;
}
