package com.example.ositopolarapp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ositopolarapp.R
import com.example.ositopolarapp.ui.theme.OsitoBluePrimary
import com.example.ositopolarapp.ui.theme.OsitoWhite

@Composable
fun OsitoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = OsitoBluePrimary,
            contentColor = OsitoWhite
        ),
        shape = RoundedCornerShape(50), // Bordes completamente redondeados
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono del oso a la izquierda (asegúrate de tener este recurso)
            Icon(
                painter = painterResource(id = R.drawable.bear_logo_white),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = OsitoWhite
            )

            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Icono de flecha a la derecha
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = OsitoWhite
            )
        }
    }
}