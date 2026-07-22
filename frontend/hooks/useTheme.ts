import { useColors as useColorHooks } from "./use-colors";

export function useColors() {
  return useColorHooks();
}

export function useColorScheme() {
  return "dark" as const;
}

export function useTheme() {
  return useColorHooks();
}
