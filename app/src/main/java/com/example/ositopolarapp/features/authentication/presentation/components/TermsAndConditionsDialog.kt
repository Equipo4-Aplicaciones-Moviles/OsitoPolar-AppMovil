package com.example.ositopolarapp.features.authentication.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun TermsAndConditionsDialog(
    onAccept: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Título
                Text(
                    text = "Términos y Condiciones",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(16.dp))

                // Contenido scrolleable
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "TÉRMINOS Y CONDICIONES DE USO DE OSITOPOLAR",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Última actualización: Diciembre 2024",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TermsSection(
                        title = "1. Aceptación de los Términos",
                        content = "Al acceder y utilizar la aplicación OsitoPolar, usted acepta estar sujeto a estos Términos y Condiciones de Uso. Si no está de acuerdo con alguna parte de estos términos, no podrá acceder al servicio."
                    )

                    TermsSection(
                        title = "2. Descripción del Servicio",
                        content = "OsitoPolar es una plataforma que conecta proveedores de servicios de refrigeración con clientes que necesitan mantenimiento, instalación o reparación de equipos de frío. La aplicación permite gestionar equipos, solicitar servicios, realizar pagos y acceder a análisis de rendimiento."
                    )

                    TermsSection(
                        title = "3. Registro y Cuenta de Usuario",
                        content = "Para utilizar nuestros servicios, debe crear una cuenta proporcionando información veraz y actualizada. Usted es responsable de mantener la confidencialidad de su cuenta y contraseña, así como de todas las actividades que ocurran bajo su cuenta."
                    )

                    TermsSection(
                        title = "4. Uso Aceptable",
                        content = "Usted se compromete a utilizar la plataforma únicamente para fines legales y de acuerdo con estos términos. Queda prohibido:\n\n• Usar el servicio para actividades ilegales\n• Intentar acceder a cuentas de otros usuarios\n• Transmitir virus o código malicioso\n• Interferir con el funcionamiento del servicio\n• Proporcionar información falsa o engañosa"
                    )

                    TermsSection(
                        title = "5. Pagos y Facturación",
                        content = "Los pagos se procesan a través de proveedores de pago seguros (Stripe). Al proporcionar información de pago, usted garantiza que está autorizado a usar dicho método de pago. Las tarifas y precios están sujetos a cambios con previo aviso."
                    )

                    TermsSection(
                        title = "6. Política de Privacidad",
                        content = "Su privacidad es importante para nosotros. Recopilamos y utilizamos su información personal de acuerdo con nuestra Política de Privacidad. Al usar OsitoPolar, usted consiente la recopilación y uso de información según lo descrito en dicha política."
                    )

                    TermsSection(
                        title = "7. Propiedad Intelectual",
                        content = "Todo el contenido de la aplicación, incluyendo pero no limitado a textos, gráficos, logos, iconos, imágenes y software, es propiedad de OsitoPolar o sus licenciantes y está protegido por leyes de propiedad intelectual."
                    )

                    TermsSection(
                        title = "8. Limitación de Responsabilidad",
                        content = "OsitoPolar no será responsable por daños indirectos, incidentales, especiales o consecuentes que resulten del uso o la imposibilidad de usar el servicio. Nuestra responsabilidad total no excederá el monto pagado por usted en los últimos 12 meses."
                    )

                    TermsSection(
                        title = "9. Modificaciones",
                        content = "Nos reservamos el derecho de modificar estos términos en cualquier momento. Las modificaciones entrarán en vigor inmediatamente después de su publicación en la aplicación. El uso continuado del servicio después de cualquier cambio constituye su aceptación de los nuevos términos."
                    )

                    TermsSection(
                        title = "10. Contacto",
                        content = "Si tiene preguntas sobre estos Términos y Condiciones, puede contactarnos a través de:\n\n• Email: soporte@ositopolar.com\n• Teléfono: +1 (555) 123-4567"
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(16.dp))

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Rechazar")
                    }

                    Button(
                        onClick = onAccept,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Aceptar")
                    }
                }
            }
        }
    }
}

@Composable
private fun TermsSection(
    title: String,
    content: String
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
