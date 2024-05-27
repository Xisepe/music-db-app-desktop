package screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import model.AsyncResult
import model.response.ErrorResponse

@Composable
fun <T> AsyncResultHandler(
    asyncResult: AsyncResult<T>,
    onError: @Composable (ErrorResponse) -> Unit = { error ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = error.msg,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
    },
    onLoading: @Composable () -> Unit = {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    },
    onSuccess: @Composable (T) -> Unit
) {
    when (asyncResult) {
        is AsyncResult.Loading -> onLoading()
        is AsyncResult.Success -> onSuccess(asyncResult.data)
        is AsyncResult.Error -> onError(asyncResult.error)
    }
}