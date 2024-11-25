package com.example.gymsmart.components.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MoveLeft
import com.example.gymsmart.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun AttachmentsPage(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attachments") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Lucide.MoveLeft,
                            contentDescription = "Move Back to Previous Page"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()) { innerPadding ->
       // Image(painter = painterResource(id = R.drawable.gymblck), contentDescription = null, contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally// Add spacing between items
    
            
        ){

            AsyncImage(model = "https://m.media-amazon.com/images/I/61+UlXddzxL._AC_SL1000_.jpg",
                       contentDescription = "GymStraps",
                        )
            Text(text = "GymStraps")
            Text(text = "GymStraps are most commonly used for back excercises or pulling workouts. Types of workouts include, deadlifts, back rows, cable back rows and shoulder shrugs. ")
            //Include images here
            Row {


                Image(
                    painter = painterResource(id = R.drawable.romanian_deadlift_800),
                    contentDescription = null,
                    modifier = Modifier
                        .alignByBaseline()
                        .size(120.dp)

                )

                Image(
                    painter = painterResource(id = R.drawable.wide_grip_lat_pulldown_muscles_worked_png),
                    contentDescription = null,
                    modifier = Modifier
                        .alignByBaseline()
                        .size(135.dp)
                )
            }
            val uriHandler = LocalUriHandler.current
            Button(onClick = { uriHandler.openUri("https://www.amazon.com/Gymreapers-Weightlifting-Bodybuilding-Powerlifting-Deadlifts/dp/B07BB3VQ42/ref=sr_1_1_sspa?crid=1MLOYXXSN9CGO&dib=eyJ2IjoiMSJ9.cvqkWC9MIdB2zHuuQsDEK38wJpCD2M_nympNbQCrISN6ta0BjJmrU4oxDXOoYnapIIo3h_JB8DEho-3R7Wtur6raSEHz-gvntM3tnlvtnJIgrYSI1QYyFJce-kJM8ovOvl3UVm7cLX0NEP-BkZnW2osQ45i0JP1I-uhaYMI0nVkJnXQXg4rs6lVz0M8nGQCB6Y6_OGR7XvHjoTZWpmqZwj8nJFyeGijXhJUY7ozxqM_dMqx2Hu7q6feyx7WjsAI5stepiutHeqkQmVFlrMRiIHeBY7ad6sjX-UFVYkFhqQA.SJR2Uhp-BMs-AsYiV0UZGRwsF4-M_dbeaX_csN8nr8k&dib_tag=se&keywords=gym%2Bstraps&qid=1730837777&sprefix=gymst%2Caps%2C140&sr=8-1-spons&sp_csd=d2lkZ2V0TmFtZT1zcF9hdGY&th=1")})
            {
                Text(text = "Click here to buy")
            }
            
            Text(text = "")
            Text(text = "")
            
            AsyncImage(model = "https://m.media-amazon.com/images/I/71W4knhV1tL._AC_SL1483_.jpg",
                contentDescription = "Wrist Straps")
            Text(text = "Wrist Straps")
            Text(text = "Wrist Straps are used for any sort of push movement. They are used when the amount of weight you are pushing is alot, or maybe if your wrists arent used to the weight. Most commonly used for excercises such as bench press, dumbell press, and dips")
            Row {
                Image(painter = painterResource(id =R.drawable.bench),
                    contentDescription = null,
                    modifier = Modifier
                        .alignByBaseline()
                        .size(150.dp))
            }

            Button(onClick = { uriHandler.openUri("https://www.amazon.com/Hustle-Wrist-Wraps-Weightlifting-Bodybuilding/dp/B0749G1LNH/ref=sr_1_3_sspa?crid=1P5BJQ9C0WBOQ&dib=eyJ2IjoiMSJ9.IDuuX-olFK2H5Ah4WbnIuU_Tpbuu2fnH_iB862zBTCSSBuXllKY0s-lgSl4kUSoTZmI-7_AgmIqzj1WvOm_CVQ1fSUfFATL4TDoGcPXtbeQhaiYfTlYvl5eV1389Vl75FV17wsGjq5ipYNfCEuiaUB41zUIZWoD6Oqb3YLkau-_kVQRfnA9XQEA4pg7f6JelLkHVrSDpgHLeqavQNwDlWhBdYxb5aXCZJFb3VRSb1k_VYzfq8-7hLQvr3SCRxa9zxhAXE6Q1ERN3USyxt7EDpUAIbEOs0Bmmkp-Jdog6Yh4.bgpZHFExyKeNTe2JSCQ4blHIyNNK3q3TchYuQCtF-5g&dib_tag=se&keywords=weightlifting%2Bstraps&qid=1730838308&sprefix=weightlifting%2Bstrap%2Caps%2C160&sr=8-3-spons&sp_csd=d2lkZ2V0TmFtZT1zcF9hdGY&th=1")})
                {
                Text(text = "Click here to buy")
                   }

            Text(text = "")
            Text(text = "")
            
            AsyncImage(model = "https://www.gymreapers.com/cdn/shop/files/quick-lock-belt-black-main.jpg?v=1708626069&width=2048",
                contentDescription = "Lifting belt")
            Text(text = "Lifting Belt")
            Text(text = "Lifting belts are used for any sort of full body pulling or pushing excercise that requires you to move alot of weight. Excercises usually performed with a lifting belt incluce, deadlifts, squats, bent over rows and Olympic lifts. ")
            Row {
                Image(painter = painterResource(id = R.drawable.romanian_deadlift_800) ,
                      contentDescription = null,
                    modifier = Modifier
                        .alignByBaseline()
                        .size(150.dp))

                Image(painter = painterResource(id = R.drawable.squat),
                      contentDescription = null,
                        modifier = Modifier
                            .alignByBaseline()
                            .size(150.dp))
            }
            Button(onClick = { uriHandler.openUri("https://www.amazon.com/Gymreapers-Weightlifting-Bodybuilding-Powerlifting-Training/dp/B0849ZNTWW/ref=sr_1_4_sspa?crid=1H65GNJBCR0FV&dib=eyJ2IjoiMSJ9.n0Dwz0f3V81C9Q6ev9wVR5FeMTYFJ1Psc2rFgvjHuNsAo1exr6uI90_iwfBCMAsnm4-83zFTCa497yyC-AK17WClcS-otnbRAfY-9KOLKaJx5IVpB5wzznOKnXMpaV_Ia-74tUlWqb4Lz4R0527hskYxngt9yAGzv5jVtoG5iMV9K11Mu2kVCvjMnvS13CN0pcgpl9TO4_mlIJFOjQNIZTsN8iIa4jt8Oervt_3-3Q7VoKrhK2m0nq7B5sI8WTFV-BFyfo5DvGT1OHfuyDddU7Y-zxrxrKsxSMj3Xv3pki0.CYioWLhfBQxGGy3Y7CSNdw8Pi5yqobL5Hg5uDm-m3r8&dib_tag=se&keywords=weight%2Blifting%2Bbelt&qid=1730838360&sprefix=weight%2Caps%2C158&sr=8-4-spons&sp_csd=d2lkZ2V0TmFtZT1zcF9hdGY&smid=A3TATHROASWQUD&th=1&psc=1") }) {
                Text(text = "Click here to buy")
            }

            Text(text = "")
            Text(text = "")
            
            AsyncImage(model = "https://m.media-amazon.com/images/I/61lIbzZVYmL._AC_SL1200_.jpg",
                contentDescription = "Lifting Belts for legs")
            Text(text = "Ankle Straps")
            Text(text = "Ankle straps are used for cable weight excercises. Such excercises include cable machine kickbacks, Cable hip abductors, and cable leg extensions")
            Row {
                Image(painter = painterResource(id = R.drawable.kickbacks),
                    contentDescription = null,
                    modifier = Modifier
                        .alignByBaseline()
                        .size(150.dp))

                Image(painter = painterResource(id = R.drawable.hipabductors),
                    contentDescription = null,
                    modifier = Modifier
                        .alignByBaseline()
                        .size(150.dp))
            }
            Button(onClick = {uriHandler.openUri("https://www.amazon.com/Vishusju-Neoprene-Kickback-Machines-Adjustable/dp/B07R3JMWCF/ref=asc_df_B07R3JMWCF?mcid=d81b805e9276343986903234cc0f0b47&tag=hyprod-20&linkCode=df0&hvadid=693769125017&hvpos=&hvnetw=g&hvrand=301484351524118681&hvpone=&hvptwo=&hvqmt=&hvdev=c&hvdvcmdl=&hvlocint=&hvlocphy=9031618&hvtargid=pla-802579847975&th=1")}) {
                Text(text = "Click here to buy")
            }

            Text(text = "")
            Text(text = "")

            


        }
    }
}