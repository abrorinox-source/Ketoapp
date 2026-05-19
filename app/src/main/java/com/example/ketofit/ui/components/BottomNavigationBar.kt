package com.example.ketofit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ketofit.navigation.BottomNavItem

@Composable
fun BottomNavigationBar(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    Surface(
        color = Color.White,
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFD7D7D7), RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 8.dp, vertical = 7.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BottomNavItem.entries.forEachIndexed { index, item ->
                BottomNavTab(
                    item = item,
                    selected = selectedIndex == index,
                    onClick = { onTabSelected(index) },
                )
            }
        }
    }
}

@Composable
private fun RowScope.BottomNavTab(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val selectedColor = when (item) {
        BottomNavItem.Lessons -> Color(0xFFB8F7D0)
        else -> Color(0xFFFFD700)
    }
    val selectedTextColor = when (item) {
        BottomNavItem.Lessons -> Color(0xFF064E3B)
        else -> Color(0xFF705E00)
    }
    val idleColor = Color(0xFF505050)

    Column(
        modifier = Modifier
            .weight(1f)
            .height(58.dp)
            .padding(horizontal = 2.dp)
            .clip(CircleShape)
            .background(if (selected) selectedColor else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            modifier = Modifier.size(22.dp),
            tint = if (selected) selectedTextColor else idleColor,
        )
        Text(
            text = item.label,
            color = if (selected) selectedTextColor else idleColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
        )
    }
}
