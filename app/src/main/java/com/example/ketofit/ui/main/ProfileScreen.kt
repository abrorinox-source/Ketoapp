package com.example.ketofit.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.HeadsetMic
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.PersonAddAlt
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ketofit.AppContainer
import com.example.ketofit.data.model.User
import com.example.ketofit.ui.theme.Primary
import com.example.ketofit.ui.theme.PrimaryContainer
import com.example.ketofit.viewmodel.ProfileViewModel
import java.util.Locale

private val PageBackground = Color(0xFFF8F8F8)
private val CardBorder = Color(0xFFE7E7E7)
private val Ink = Color(0xFF111111)
private val Muted = Color(0xFF555555)
private val TileBackground = Color(0xFFF0F0F0)
private val Teal = Color(0xFF00897B)
private val DarkGold = Color(0xFF7A6500)

@Composable
fun ProfileScreen(
    container: AppContainer,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
) {
    val viewModel: ProfileViewModel = viewModel(factory = container.viewModelFactory)
    LaunchedEffect(Unit) { viewModel.loadProfile() }
    val profile = viewModel.profile

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PageBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ProfileTopBar()
        ProfileTitle()

        if (profile == null) {
            Box(modifier = Modifier.fillMaxWidth().padding(top = 42.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryContainer)
            }
        } else {
            ProfileHeroCard(profile = profile)
            SubscriptionCard(profile = profile)
            PersonalInfoCard(profile = profile)
            SettingsSection(profile = profile)
            SupportCard()
            LogoutButton { viewModel.logout(onLogout) }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun ProfileTopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = Icons.Outlined.Menu, contentDescription = "Menu", tint = Primary, modifier = Modifier.size(24.dp))
        Text(text = "KETO LIFE", color = Primary, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleLarge)
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(PrimaryContainer, CircleShape)
                .padding(2.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = "Profile", tint = Primary, modifier = Modifier.size(30.dp))
            }
        }
    }
}

@Composable
private fun ProfileTitle() {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(text = "Profil", color = Ink, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
        Text(text = "Hisobingiz, obunangiz va sozlamalaringiz.", color = Muted, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun ProfileHeroCard(profile: User) {
    ProfileSurface {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(82.dp)
                    .border(3.dp, Teal, CircleShape)
                    .padding(5.dp)
                    .background(Color(0xFFEDEDED), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = null, tint = Teal, modifier = Modifier.size(66.dp))
            }
            Text(text = profile.name, color = Ink, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
            Text(text = profile.email, color = Muted, style = MaterialTheme.typography.bodyLarge)
            StatusPill(active = profile.subscription.active)
            Text(text = profile.subscription.endsAt?.let { "Tugash: $it" } ?: "Faol obuna", color = Muted, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun StatusPill(active: Boolean) {
    Surface(shape = CircleShape, color = if (active) Color(0xFFE0F5F1) else TileBackground) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Icon(imageVector = Icons.Outlined.WorkspacePremium, contentDescription = null, tint = Teal, modifier = Modifier.size(14.dp))
            Text(
                text = if (active) "Premium faol" else "Free",
                color = Teal,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun SubscriptionCard(profile: User) {
    ProfileSurface {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(text = "J O R I Y  O B U N A", color = Muted, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text(text = profile.subscription.planName.ifBlank { "30 kunlik keto kurs" }, color = Ink, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                }
                Surface(shape = RoundedCornerShape(8.dp), color = PrimaryContainer) {
                    Text(
                        text = if (profile.subscription.active) "Faol" else "Nofaol",
                        color = Primary,
                        modifier = Modifier.padding(horizontal = 13.dp, vertical = 7.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                SubscriptionDate(label = "BOSHLANISH", value = "Bugun", modifier = Modifier.weight(1f))
                Box(modifier = Modifier.height(38.dp).width(1.dp).background(CardBorder))
                SubscriptionDate(label = "TUGASH", value = profile.subscription.endsAt ?: "30 kun", modifier = Modifier.weight(1f))
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFCBB97C), RoundedCornerShape(12.dp))
                    .clickable { },
            ) {
                Text(
                    text = "Obunani boshqarish",
                    color = Primary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 14.dp),
                )
            }
        }
    }
}

@Composable
private fun SubscriptionDate(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(text = label, color = Muted, style = MaterialTheme.typography.labelMedium)
        Text(text = value, color = Ink, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PersonalInfoCard(profile: User) {
    ProfileSurface {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Shaxsiy ma'lumotlar", color = Ink, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                Icon(imageVector = Icons.Outlined.PersonAddAlt, contentDescription = "Edit profile", tint = Teal)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                InfoTile(label = "JINS", value = profile.gender, modifier = Modifier.weight(1f))
                InfoTile(label = "YOSH", value = profile.age.toString(), modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                InfoTile(label = "BO'Y", value = "${formatNumber(profile.heightCm)} cm", modifier = Modifier.weight(1f))
                InfoTile(label = "VAZN", value = "${formatNumber(profile.currentWeightKg)} kg", modifier = Modifier.weight(1f))
            }
            TargetWeightCard(profile = profile)
            Button(
                onClick = { },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGold, contentColor = Color.White),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp),
            ) {
                Text(text = "Ma'lumotlarni tahrirlash", fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun InfoTile(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(TileBackground, RoundedCornerShape(13.dp))
            .padding(horizontal = 14.dp, vertical = 13.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(text = label, color = Muted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        Text(text = value, color = Ink, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun TargetWeightCard(profile: User) {
    val total = (profile.currentWeightKg - profile.targetWeightKg).coerceAtLeast(1.0)
    val percent = ((profile.currentWeightKg - profile.targetWeightKg).coerceAtLeast(0.0) / total * 75).toInt().coerceIn(0, 75)
    Surface(shape = RoundedCornerShape(13.dp), color = Color(0xFFFFF4C4), modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(text = "MAQSADLI VAZN", color = Primary, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Text(text = "${formatNumber(profile.targetWeightKg)} kg", color = Primary, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Medium)
            }
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { percent / 100f },
                    color = Primary,
                    trackColor = Color(0xFFE5D58B),
                    strokeWidth = 5.dp,
                    modifier = Modifier.size(44.dp),
                )
                Text(text = "$percent%", color = Primary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun SettingsSection(profile: User) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Sozlamalar", color = Ink, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, modifier = Modifier.padding(start = 3.dp))
        ProfileSurface {
            Column(modifier = Modifier.fillMaxWidth()) {
                SettingRow(icon = Icons.Outlined.Language, title = "Til", value = profile.language.ifBlank { "O'zbekcha" })
                SettingRow(icon = Icons.Outlined.NotificationsActive, title = "Eslatmalar", value = if (profile.reminderEnabled) "Yoqilgan" else "O'chirilgan", valueColor = Teal)
                SettingRow(icon = Icons.Outlined.WaterDrop, title = "Suv eslatmasi", value = "Kuniga 6 marta")
                SettingSwitchRow()
            }
        }
    }
}

@Composable
private fun SettingRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    valueColor: Color = Muted,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(imageVector = icon, contentDescription = null, tint = Teal, modifier = Modifier.size(20.dp))
                Text(text = title, color = Ink, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            }
            Text(text = value, color = valueColor, style = MaterialTheme.typography.bodyLarge)
        }
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
    }
}

@Composable
private fun SettingSwitchRow() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 9.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(imageVector = Icons.Outlined.DarkMode, contentDescription = null, tint = Teal, modifier = Modifier.size(20.dp))
            Text(text = "Dark mode", color = Ink, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
        Switch(
            checked = false,
            onCheckedChange = null,
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE6E6E6),
                uncheckedBorderColor = Color.Transparent,
            ),
        )
    }
}

@Composable
private fun SupportCard() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFEFF8F7),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF8ED3CB), RoundedCornerShape(16.dp)),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(modifier = Modifier.size(42.dp).background(Color(0xFFCFEFED), CircleShape), contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Outlined.HeadsetMic, contentDescription = null, tint = Teal)
            }
            Text(text = "Yordam kerakmi?", color = Teal, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Text(text = "Admin bilan bog'laning", color = Muted, style = MaterialTheme.typography.bodyLarge)
            Button(
                onClick = { },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009688), contentColor = Color.White),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp),
            ) {
                Text(text = "Supportga yozish", fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun LogoutButton(onLogout: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onLogout)
            .padding(vertical = 15.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = Icons.Outlined.Logout, contentDescription = null, tint = Color(0xFFD00000), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Chiqish", color = Color(0xFFD00000), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ProfileSurface(content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CardBorder, RoundedCornerShape(18.dp)),
        content = content,
    )
}

private fun formatNumber(value: Double): String {
    val rounded = value.toInt().toDouble()
    return if (value == rounded) value.toInt().toString() else String.format(Locale.US, "%.1f", value)
}
