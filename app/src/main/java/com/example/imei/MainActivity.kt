package com.example.imei

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.imei.ui.theme.ImeiTheme
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import org.json.JSONObject
import org.w3c.dom.Text


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImeiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
            }
        }
    }
}


/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ImeiTheme {
        Greeting("Android")
    }
}

 */

@Composable
fun recaptcha() {

    // creating a variable for context
    val ctx = LocalContext.current

    // creating a variable for site key.
    var SITE_KEY = "6LfMaFsmAAAAAJ4l-8RFwlWj1rVdBEDtVIXFcI1y"

    // creating a column for
    // displaying a text and a button.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth(),

        // specifying vertical and horizontal alignment.
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // specifying text for a text view
        Text(
            text = "reCAPTCHA in Android",
            color = Color.Blue,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
        )
        // adding a spacer on below line.
        Spacer(modifier = Modifier.height(5.dp))

        // creating a button to verify the user.
        Button(
            // adding on click for the button.
            onClick = {
                // calling safety net on below line to verify recaptcha.
                SafetyNet.getClient(ctx).verifyWithRecaptcha(SITE_KEY).addOnSuccessListener {
                    if (it.tokenResult!!.isNotEmpty()) {
                        // calling handle verification method
                        // to handle verification.
                        handleVerification(it.tokenResult.toString(), ctx)
                    }
                }.addOnFailureListener {
                    // on below line handling exception
                    if (it is ApiException) {
                        val apiException = it as ApiException
                        // below line is use to display an
                        // error message which we get.
                        Log.d(
                            "TAG", "Error message: " +
                                    CommonStatusCodes.getStatusCodeString(apiException.statusCode)
                        )
                    } else {
                        // below line is use to display a toast message for any error.
                        Toast.makeText(ctx, "Error found is : $it", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            },
            // on below line adding
            // modifier for our button.
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // on below line specifying text for button.
            Text(text = "Verify Captcha", modifier = Modifier.padding(8.dp))
        }
    }
}


// creating a method to handle verification.
fun handleVerification(responseToken: String, ctx: Context) {
    // inside handle verification method we are
    // verifying our user with response token.
    // url to sen our site key and secret key
    // to below url using POST method.
    val url = "https://www.google.com/recaptcha/api/siteverify"

    // creating a new variable for our request queue
    val queue = Volley.newRequestQueue(ctx)
    //val SECRET_KEY = "Enter your secret key"
    val SECRET_KEY = "6LfMaFsmAAAAAGDvtZzc_fZxi39DgjYu_lxUdHs8"

    val request: StringRequest =
        object : StringRequest(Request.Method.POST, url, object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                // inside on response method we are checking if the
                // response is successful or not.
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("success")) {
                        // if the response is successful
                        // then we are showing below toast message.
                        Toast.makeText(
                            ctx,
                            "User verified with reCAPTCHA",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // if the response if failure
                        // we are displaying
                        // a below toast message.
                        Toast.makeText(
                            ctx,
                            jsonObject.getString("error-codes").toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (ex: Exception) {
                    // if we get any exception then we are
                    // displaying an error message in logcat.
                    Log.d("TAG", "JSON exception: " + ex.message)
                }
            }
        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
                // displaying toast message on response failure.
                Toast.makeText(ctx, "Fail to post data..", Toast.LENGTH_SHORT)
                    .show()
            }
        }) {
            override fun getParams(): Map<String, String>? {

                val params: MutableMap<String, String> = HashMap()
                params["secret"] = SECRET_KEY
                params["response"] = responseToken
                return params
            }
        }
    // below line of code is use to set retry
    // policy if the api fails in one try.
    request.setRetryPolicy(
        DefaultRetryPolicy( // we are setting time for retry is 5 seconds.
            50000,  // below line is to perform maximum retries.
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
    )
    // below line is to make
    // a json object request.
    queue.add(request)

}

@Composable
fun IconQuestions(modifier: Modifier){
    Row() {
        IconButton(
            onClick = { /*TODO*/ },
            enabled = false,
            modifier = Modifier,

        ) {
            Icon(Icons.Filled.Warning, contentDescription = "Localized description")

            
        }
        
    }
}

@Composable
fun ImageAsIcon(
    imageResId: Int,
    contentDescription: String,
    size: Dp,
    modifier: Modifier = Modifier
) {
    val image: Painter = painterResource(imageResId)

    Image(
        painter = image,
        contentDescription = contentDescription,
        modifier = modifier.size(size)

    )
}
@Composable
fun LogoImage(modifier: Modifier){
    Row(modifier = Modifier.padding(start = 30.dp, end = 40.dp)) {
        Image(
            painter = painterResource(R.drawable.logo_osiptel),
            contentDescription = "Contact profile picture",
            modifier = Modifier,
        )
        
    }


}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextoInfo(modifier: Modifier){


    val textState = remember { mutableStateOf(TextFieldValue()) }

    /*
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(16.dp)
    ) {
        Text(
            text = "Este aplicativo permite consultar en línea el código IMEI o número de servicio",
            color= Color.Black
        )
        
    }

     */

    Row(modifier = Modifier
        .padding(16.dp)

        //#0066B1
        .border(width = 1.dp, color = Color(android.graphics.Color.parseColor("#00AAE8")))
        .background(color = Color(android.graphics.Color.parseColor("#01B6ED")))
        ) {
        Text(
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 16.dp, end = 16.dp),
            text = "Este aplicativo permite consultar en línea el código IMEI o número de servicio",
            color = Color.White
        )
    }

    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center) {
        /*
        TextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            placeholder = { Text("Consultar IMEI o número de servicio") },
            //singleLine = true,

            modifier = Modifier
                .weight(1f) //para que ocupe todo el espacio del row

                .border(width = 1.dp, color = Color.Gray),
        )
         */

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = textState.value,
            onValueChange ={textState.value = it} ,
            placeholder = { Text("¿Consultar IMEI o número de servicio?") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
            )
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center)
    {

        Button(onClick = { /*TODO*/ }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(Color(android.graphics.Color.parseColor("#599BCC")))) {
            Text(text = "Verificar")
        }
    }
}

@Preview (showBackground = true)
@Composable
fun Preview(){
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            ImageAsIcon(
                imageResId = R.drawable.solicitud,
                contentDescription = "My Image",
                size = 40.dp,
                modifier = Modifier
                    //.padding(8.dp)
                    .clickable { /* Acción al hacer clic en el icono */ }
            )
        }
            Spacer(modifier = Modifier.height(10.dp))
            LogoImage(modifier = Modifier.padding(start = 10.dp))
            Spacer(modifier = Modifier.height(10.dp))
            //recaptcha()
            TextoInfo(modifier = Modifier)
            Spacer(modifier = Modifier.height(10.dp))
    }
    //IconQuestions(modifier = Modifier)
}


////////////////////////////////////////////////////////////////////////////////////////
@Preview (showBackground = true)
@Composable
fun PreviewRespuesta(){
    Column {
        CajaDeComposes()
        
    }



}

@Composable
fun ImageRespuesta(){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Image(
            painter = painterResource(R.drawable.search),
            contentDescription = "Contact profile picture",
            modifier = Modifier,
            alignment = Alignment.Center
        )
    }
}
@Composable
fun CampoInformacionRespuesta(){
    Row(
        modifier = Modifier.fillMaxWidth(),

        verticalAlignment = Alignment.CenterVertically,

    ) {
        Text(
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 16.dp, end = 16.dp),
            text = "La respuesta a su consulta es :",

            fontWeight = FontWeight.Bold,
            color = Color(android.graphics.Color.parseColor("#599BCC")),
            style = TextStyle(fontSize = 23.sp)

        )
    }
}

@Composable
fun CampoNumero(){
    Row(modifier = Modifier) {
        Text(
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 16.dp, end = 16.dp),
            text ="El número :", color = Color.Black)
    }
}

@Composable
fun CampoNumeroImei(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            //modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 16.dp, end = 16.dp),
            text =" 1234567890123456",
            color = Color.Black,
            fontWeight = FontWeight.Bold,

        )
    }
}
@Composable
fun InformacionRespuesta(){
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 16.dp, end = 16.dp),
            text = "El IMEI no se encuentra asociado a ningún terminal reportado")
    }
}
@Composable
fun ButtonRegresar(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        //verticalAlignment = Alignment.CenterVertically

    ) {
        OutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = { /*TODO*/ }

        ) {
            ImageAsIcon2(
                imageResId = R.drawable.chevron__1_,
                contentDescription = "regresar",
                size = 15.dp )

            Text(
                text = "Regresar",
                color = Color(android.graphics.Color.parseColor("#599BCC")),
            )
        }
    }

}



@Composable
fun ImageAsIcon2(
    imageResId: Int,
    contentDescription: String,
    size: Dp,
    modifier: Modifier = Modifier
) {
    val image: Painter = painterResource(imageResId)

    Image(
        painter = image,
        contentDescription = contentDescription,
        modifier = modifier.size(size)

    )
}

@Composable
fun CajaDeComposes(){
    Box(modifier = Modifier
        .padding(16.dp)
        .background(Color.White)
            /*
        .border(
            border = BorderStroke(
                width = 10.dp,
                color = Color.Gray
            )
        )

             */

    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(50.dp))
            ImageRespuesta()
            CampoInformacionRespuesta()
            CampoNumero()
            CampoNumeroImei()
            InformacionRespuesta()
            ButtonRegresar()
        }
    }
}
/////////////////////////////////////////////////////////////////////////////////////////
@Preview (showBackground = true)
@Composable
fun PreviewPreguntasFrecuentes(){
    ScreenPreguntasFrecuentas()

}
val datos: List<DataApp> = listOf(
    DataApp("PrimeroPrimeroPrimeroPrimeroPrimeroPrimeroPrimero", "Subtitulo 1"),
    DataApp("Segundo", "SubtituloSubtituloSubtituloSubtituloSubtituloSubtitulo 2"),
    DataApp("Tercero", "Subtitulo 3"),
    DataApp("Cuarto", "Subtitulo 4"),
    DataApp("Quinto", "Subtitulo 5"),
    DataApp("Sexto", "SubtituloSubtituloSubtituloSubtituloSubtitulo 6"),
    DataApp("Septimo", "Subtitulo 7"),
    DataApp("Octavo", "Subtitulo 8"),
    DataApp("Noveno", "Subtitulo 9"),
    DataApp("Décimo", "Subtitulo 10"),
    DataApp("Onceavo", "Subtitulo 11"),
    DataApp("Doceavo", "Subtitulo 12"),
    DataApp("Treceavo", "Subtitulo 13")
)


@Composable
fun ScreenPreguntasFrecuentas(){
    Column() {
        textoPrincipalPreguntasFrecuentes()
        Spacer(modifier = Modifier.height(50.dp))
        ImagePrincipalPreguntasFrecuents()
        Spacer(modifier = Modifier.height(50.dp))
        ListaPreguntasFrecuentes(datos = datos)
        Spacer(modifier = Modifier.height(300.dp))
    }

}

@Composable
fun textoPrincipalPreguntasFrecuentes(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Text(
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 16.dp, end = 16.dp),
            text = "Preguntas frecuentes",

            fontWeight = FontWeight.Bold,
            color = Color(android.graphics.Color.parseColor("#599BCC")),
            style = TextStyle(fontSize = 23.sp)

        )
    }

}
@Composable
fun ImagePrincipalPreguntasFrecuents(){
    Row(modifier = Modifier.fillMaxWidth(),Arrangement.Center) {
        Image(
            painter = painterResource(id = R.drawable.solicitud),
            contentDescription = "Preguntas Frecuentes")
    }
}

data class DataApp(val pregunta: String, val respuesta: String)


@Composable
fun ListaPreguntasFrecuentes(
    datos: List<DataApp>
){
    LazyColumn() {
        items(items = datos){ dato ->
            CardGeneral(pregunta = dato.pregunta, respuesta = dato.respuesta)
        }
    }
}

@Composable
private fun CardGeneral(
    pregunta: String,
    respuesta: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(pregunta,respuesta)
    }
}


@Composable
private fun CardContent(pregunta: String,respuesta: String) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            //Text(text = "Hello, ")
            Text(
                text = pregunta,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (expanded) {
                Text(
                    text = (respuesta),
                )
            }
        }

        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                //imageVector = if (expanded) Icons.Filled.ArrowBack else Icons.Filled.ArrowDropDown,
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                }
            )
        }

    }
}


















