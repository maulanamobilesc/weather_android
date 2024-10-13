package com.maulana.warehouse.core.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maulana.weathermobile.util.GlobalDimension

@Composable
fun WarehouseItem(title:String, content:String){
    Row(
        Modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = Color.DarkGray)
            .padding(GlobalDimension.sectionPadding), verticalAlignment = Alignment.CenterVertically
    ) {
        /*AsyncImage(
            model = "",
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .size(size = 50.dp)
                .clip(RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp))
        )*/
        Icon(Icons.Filled.Face, contentDescription = "warehouse icon")

        Spacer(8.dp)
        Column(Modifier.fillMaxWidth()) {
            Text(
                text = title,
                fontSize = GlobalDimension.defaultFontSize,
                fontWeight = FontWeight(800)
            )
            Text(
                text = content,
                fontSize = GlobalDimension.smallFontSize
            )
        }
    }
}