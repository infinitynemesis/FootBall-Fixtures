package com.kryptkode.footballfixtures.competitions.detail.fixtures

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.kryptkode.footballfixtures.app.base.viewmodel.BaseViewModel
import com.kryptkode.footballfixtures.app.data.repo.AppRepository
import javax.inject.Inject

class FixturesViewModel @Inject constructor(
    private val repository: AppRepository
) : BaseViewModel() {

    private val blank = MutableLiveData<Int>()
    private var loaded = false

    private val repoResult = Transformations.map(blank) {
        repository.getFixturesForCompetition(it)
    }

    val networkState = Transformations.switchMap(repoResult) {
        it.refreshState
    }

    val repoList = Transformations.switchMap(repoResult) {
        it.data
    }

    val listEmpty = Transformations.map(repoList) {
        it.isEmpty()
    }

    fun refresh() {
        val repos = repoResult.value
        repos?.refresh?.invoke()
    }

    fun loadData(competitionId: Int?) {
        loadIfNotLoaded(competitionId)
    }

    private fun loadIfNotLoaded(competitionId: Int?) {
        if (!loaded) {
            blank.value = competitionId
            loaded = true
        }
    }

}