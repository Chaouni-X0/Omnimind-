import re

with open('app/src/main/java/com/example/data/apipool/ApiPoolManager.kt', 'r') as f:
    content = f.read()

new_selectActiveKey = """
    suspend fun selectActiveKey(excludeKeyIds: Set<String> = emptySet()): ApiKeyConfig? {
        val keys = repository.getKeys().filter { it.isEnabled && !excludeKeyIds.contains(it.id) }
        if (keys.isEmpty()) return null

        // Check budgets first
        val keysWithinBudget = keys.filter { key ->
            val limit = key.monthlyBudgetCents
            limit == null || key.currentSpentCents < limit
        }
        if (keysWithinBudget.isEmpty()) return null

        // Weighted random selection
        val totalWeight = keysWithinBudget.sumOf { it.priorityWeight }
        if (totalWeight <= 0) return keysWithinBudget.randomOrNull()
        
        var randomPoint = Random.nextInt(totalWeight)
        for (key in keysWithinBudget) {
            randomPoint -= key.priorityWeight
            if (randomPoint < 0) {
                return key
            }
        }
        return keysWithinBudget.lastOrNull()
    }
"""

content = re.sub(r'suspend fun selectActiveKey\(\): ApiKeyConfig\? \{.*?return keysWithinBudget\.lastOrNull\(\)\n    \}', new_selectActiveKey.strip(), content, flags=re.DOTALL)

with open('app/src/main/java/com/example/data/apipool/ApiPoolManager.kt', 'w') as f:
    f.write(content)
