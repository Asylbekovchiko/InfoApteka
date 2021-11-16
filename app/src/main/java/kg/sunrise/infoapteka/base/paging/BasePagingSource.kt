package kg.sunrise.infoapteka.base.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.Response

private const val STARTING_PAGE_INDEX = 1

abstract class BasePagingSource<ItemsData : Any, Item : Any>(
    private val getData: suspend (Int) -> Response<ItemsData>?
) : PagingSource<Int, Item>() {

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = getData(position) ?: throw Throwable()

            var nextKey: Int? = null
            var items = ArrayList<Item>()

            if (response.isSuccessful) {
                val responseBody = response.body()!!

                nextKey = getNextKey(responseBody)
                items = getResponseItem(responseBody)
            }

            LoadResult.Page(
                data = items,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position,
                nextKey = nextKey
            )
        } catch (e: Throwable) {
            return LoadResult.Error(e)
        }
    }

    abstract fun getNextKey(responseBody: ItemsData): Int?

    abstract fun getResponseItem(responseBody: ItemsData): ArrayList<Item>
}