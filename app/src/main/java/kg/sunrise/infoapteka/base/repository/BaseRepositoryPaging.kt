package kg.sunrise.infoapteka.base.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kg.sunrise.infoapteka.base.paging.BasePagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

abstract class BaseRepositoryPaging(
    defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRepository(defaultDispatcher) {

    protected fun<Items: Any, Item: Any> getDataByPage(
        pageSize: Int,
        basePagingSource: BasePagingSource<Items, Item>
    ): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false ,
            ),
            pagingSourceFactory = {
                basePagingSource
            }
        ).flow
    }
}

