import re

with open('app/src/main/java/com/example/presentation/components/PremiumUI.kt', 'r') as f:
    content = f.read()

# Separate imports from the rest
imports = re.findall(r'^import .*$', content, re.MULTILINE)
rest = re.sub(r'^import .*$\n', '', content, flags=re.MULTILINE)

# Add our missing imports
imports.extend([
    'import androidx.compose.animation.core.animateFloatAsState',
    'import androidx.compose.foundation.clickable',
    'import androidx.compose.foundation.interaction.MutableInteractionSource',
    'import androidx.compose.foundation.interaction.collectIsPressedAsState',
    'import androidx.compose.runtime.getValue',
    'import androidx.compose.runtime.remember',
    'import androidx.compose.ui.composed',
    'import androidx.compose.ui.draw.scale',
    'import com.example.ui.theme.Motion'
])

# Remove duplicates
imports = sorted(list(set(imports)))

# Write back
with open('app/src/main/java/com/example/presentation/components/PremiumUI.kt', 'w') as f:
    f.write('package com.example.presentation.components\n\n')
    for imp in imports:
        if imp != 'import ':
            f.write(imp + '\n')
    f.write('\n')
    
    # remove package declaration from rest if it exists
    rest = re.sub(r'^package .*\n', '', rest)
    f.write(rest.strip() + '\n')
