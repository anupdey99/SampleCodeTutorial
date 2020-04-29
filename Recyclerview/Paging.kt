
//PagingData
    private var currentPage: Int = PaginationListener.pageStart
    private var totalPage = 10
    private var isLastPage = false
    private var isLoading = false



    recyclerView.addOnScrollListener(object : PaginationListener(linearLayoutManager){
            override fun loadMoreItems() {
                currentPage++
                if (currentPage <= totalPage){
                    isLoading = true
                    loadData(currentPage, searchKey)
                }
            }
            override fun isLastPage(): Boolean {
                return isLastPage
            }
            override fun isLoading(): Boolean {
                return isLoading
            }
        })


// Lazy loading
    private var isLoading = false
    private val visibleThreshold = 5
    private var totalCount: Int = 0
    private var firstCall: Int = 0

binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    Timber.d("addOnScrollListener onScrolled: $dx $dy")
                    val currentItemCount = layoutManagerLinear.itemCount
                    val lastVisibleItem = layoutManagerLinear.findLastVisibleItemPosition()
                    Timber.d("onScrolled: \nItemCount: $currentItemCount  <= lastVisible: $lastVisibleItem firstCall : $firstCall  < TotalDeal : $totalCount  ${!isLoading}")
                    if (!isLoading && currentItemCount <= lastVisibleItem + visibleThreshold && firstCall < totalCount) {
                        isLoading = true
                        viewModel.loadOrderOrSearch(firstCall, 20)
                    }
                }
            }
        })



viewModel.loadOrderOrSearch()
        viewModel.pagingState.observe(viewLifecycleOwner, Observer {
            if (it.isInitLoad) {
                Timber.d("initData: ${it.dataList}")
                dataAdapter.loadInitData(it.dataList)
            } else {
                isLoading = false
                firstCall += 20
                Timber.d("loadMoreData: ${it.dataList}")
                dataAdapter.loadMoreData(it.dataList)
            }
        })