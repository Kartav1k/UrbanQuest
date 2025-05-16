

/*@OptIn(ExperimentalCoroutinesApi::class)
class RecommendationViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchRecommendations sets data on success`() = runTest {
        val fakeFetcher: suspend (List<String>) -> List<ItemFromDB> = {
            listOf(
                ItemFromDB(
                    name = "Музей искусства",
                    address = "ул. Примерная, 1",
                    matchCount = 2
                )
            )
        }

        val viewModel = RecommendationViewModel(fetcher = fakeFetcher)

        viewModel.fetchRecommendations(listOf("музей"))

        advanceUntilIdle()

        val result = viewModel.recommendations.value
        assert(result != null && result.size == 1)
        assert(result?.get(0)?.name == "Музей искусства")
        assert(viewModel.isError.value != true)
        assert(viewModel.isLoading.value == false)
    }

    @Test
    fun `fetchRecommendations sets error on failure`() = runTest {
        val failingFetcher: suspend (List<String>) -> List<ItemFromDB> = {
            throw RuntimeException("Ошибка при загрузке данных")
        }

        val viewModel = RecommendationViewModel(fetcher = failingFetcher)

        viewModel.fetchRecommendations(listOf("музей"))

        advanceUntilIdle()

        assert(viewModel.recommendations.value == null)
        assert(viewModel.isError.value == true)
        assert(viewModel.isLoading.value == false)
    }
}*/

