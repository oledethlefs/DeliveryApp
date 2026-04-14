package com.example.deliveryapp.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deliveryapp.R
import com.example.deliveryapp.core.designsystem.theme.DeliveryAppTheme

/**
 * A standardized search bar component.
 *
 * This component provides a search input field with a leading search icon, a trailing clear icon
 * (visible when text is entered), and pre-configured keyboard actions for searching.
 *
 * @param modifier The [Modifier] to be applied to the search bar.
 * @param query The current text value to be displayed in the search bar.
 * @param onQueryChange Callback invoked when the input text changes.
 * @param onSearch Callback invoked when the user triggers the IME Search action.
 * @param placeholderText The text to be displayed when the search bar is empty.
 * @param onClearFocus Callback invoked when the search bar loses focus.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StandardSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    placeholderText: String = stringResource(id = R.string.search_placeholder),
    onClearFocus: (() -> Unit)? = null,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current


    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (!focusState.isFocused) {
                    onClearFocus?.invoke()
                }
            },
        placeholder = {
            Text(text = placeholderText)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search_icon_content_description)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = {
                    onQueryChange("")
                    keyboardController?.show()
                    focusRequester.requestFocus()
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.clear_search)
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(query)
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        singleLine = true,
        shape = MaterialTheme.shapes.extraLarge,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
        )
    )
}

@Preview(showBackground = true, name = "Empty Search Bar")
@Composable
fun StandardSearchBarEmptyPreview() {
    DeliveryAppTheme {
        var query by remember { mutableStateOf("") }
        StandardSearchBar(
            modifier = Modifier.padding(16.dp),
            query = query,
            onQueryChange = { query = it },
            onSearch = {}
        )
    }
}

@Preview(showBackground = true, name = "Filled Search Bar")
@Composable
fun StandardSearchBarFilledPreview() {
    DeliveryAppTheme {
        var query by remember { mutableStateOf("Chicken Soup") }
        StandardSearchBar(
            modifier = Modifier.padding(16.dp),
            query = query,
            onQueryChange = { query = it },
            onSearch = { }
        )
    }
}