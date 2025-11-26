package com.example.ositopolarapp.features.onboarding.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingPagerScreen(
    onOnboardingComplete: () -> Unit
) {
    val pages = listOf(
        OnboardingPage.GET_STARTED,
        OnboardingPage.SELECT_PROFILE
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = true // Permite deslizar con el dedo
    ) { pageIndex ->
        when (pages[pageIndex]) {
            OnboardingPage.GET_STARTED -> {
                // CORRECCIÓN: Usamos 'onNavigateToSelectProfile'
                GetStartedScreen(
                    onNavigateToSelectProfile = {
                        scope.launch {
                            pagerState.animateScrollToPage(pageIndex + 1)
                        }
                    }
                )
            }
            OnboardingPage.SELECT_PROFILE -> {
                // CORRECCIÓN: Usamos 'onNavigateNext'
                SelectProfileScreen(
                    onNavigateNext = {
                        // Al terminar, salimos del onboarding
                        onOnboardingComplete()
                    }
                )
            }
        }
    }
}

enum class OnboardingPage {
    GET_STARTED, SELECT_PROFILE
}