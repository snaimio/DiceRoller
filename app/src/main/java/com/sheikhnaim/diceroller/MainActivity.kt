package com.sheikhnaim.diceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sheikhnaim.diceroller.ui.theme.DiceRollerTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    // ✅ Create SoundManager instance
    private lateinit var soundManager: SoundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ✅ Initialize SoundManager
        soundManager = SoundManager(this)

        setContent {
            DiceRollerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DiceRollerApp(soundManager = soundManager)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // ✅ Release sound resources
        soundManager.release()
    }
}

@Composable
fun DiceRollerApp(soundManager: SoundManager) {
    DiceWithButtonAndImage(
        soundManager = soundManager,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
            .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun DiceWithButtonAndImage(
    soundManager: SoundManager,
    modifier: Modifier = Modifier
) {
    var result by remember { mutableIntStateOf(-1) }
    var hasRolled by remember { mutableIntStateOf(0) }

    // ✅ Release sound when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            soundManager.release()
        }
    }

    val imageResource = when {
        result == -1 && hasRolled == 0 -> R.drawable.random
        result == 1 -> R.drawable.dice_1
        result == 2 -> R.drawable.dice_2
        result == 3 -> R.drawable.dice_3
        result == 4 -> R.drawable.dice_4
        result == 5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Text
        Text(
            text = if (result == -1 && hasRolled == 0) {
                "🎲 Tap Roll!"
            } else {
                "Dice: $result"
            },
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dice Image
        Image(
            painter = painterResource(imageResource),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Roll Button
        Button(
            onClick = {
                // ✅ Play sound when rolling
                soundManager.playDiceSound()

                // Generate random number
                result = Random.nextInt(1, 7)
                hasRolled = 1
            }
        ) {
            Text(
                text = stringResource(R.string.roll),
                fontSize = 24.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiceRollerPreview() {
    DiceRollerTheme {
        DiceRollerApp(soundManager = SoundManager(null!!))
    }
}