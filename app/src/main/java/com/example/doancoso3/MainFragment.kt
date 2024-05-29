package com.example.doancoso3

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.widget.NestedScrollView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs3_ns_22ns082.ApiServices
import com.example.dacs3_ns_22ns082.AuthStore
import com.example.dacs3_ns_22ns082.Product
import com.example.dacs3_ns_22ns082.ProductRecyclerAdapter
import com.example.dacs3_ns_22ns082.Reviews
import com.example.dacs3_ns_22ns082.ReviewsRecyclerAdapter
import com.example.dacs3_ns_22ns082.Tutorial
import com.example.dacs3_ns_22ns082.TutorialRecyclerAdapter
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Timer
import java.util.TimerTask

class MainFragment : AuthFragment() {
    private lateinit var authStorage: AuthStore
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.nestedScrollView)

        // Sử dụng ViewTreeObserver để đảm bảo NestedScrollView cuộn về đầu sau khi vẽ xong
        nestedScrollView.viewTreeObserver.addOnGlobalLayoutListener {
            nestedScrollView.scrollTo(0, 0)
        }

        val imgLogout = view.findViewById<ImageView>(R.id.img_logout)
        authStorage = AuthStore(requireContext())
        imgLogout.setOnClickListener {
            authStorage.clearToken()
            authStorage.clearIsAdmin()
            findNavController().navigate(R.id.loginFragment)
        }

        val rvProduct = view.findViewById<RecyclerView>(R.id.rv_product)
        rvProduct.layoutManager = GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)
        val adapter = ProductRecyclerAdapter(requireContext(), mutableListOf())
        rvProduct.adapter=adapter
        getProducts() { products, error ->
            if (error != null || products == null) {
                Toast.makeText(
                    context,
                    "Failed to get products with error ${error?.message}",
                    Toast.LENGTH_SHORT
                ).show()
                return@getProducts
            }
            val viewP= mutableListOf<Product>()

            products.forEachIndexed { index, product ->
                if(index > 3) return@forEachIndexed
                viewP.add(product)
            }

            adapter.updateList(viewP)
        }

        val rvTutorial = view.findViewById<RecyclerView>(R.id.rv_tutorial)
        rvTutorial.layoutManager = GridLayoutManager(context, 1, RecyclerView.HORIZONTAL, false)
        val adapterT = TutorialRecyclerAdapter(requireContext(), mutableListOf())
        rvTutorial.adapter=adapterT
        getTutorials() { tutorials, error ->
            if (error != null || tutorials == null) {
                Toast.makeText(
                    context,
                    "Failed to get tutorials with error ${error?.message}",
                    Toast.LENGTH_SHORT
                ).show()
                return@getTutorials
            }
            val viewT= mutableListOf<Tutorial>()

            tutorials.forEachIndexed { index, tutorial ->
                if(index > 1) return@forEachIndexed
                viewT.add(tutorial)
            }

            adapterT.updateList(viewT)
        }

        adapter.setOnHeartClickedListener { product, pos ->
            addProductHeart(product.productId) { p, error ->
                if (error != null || p == null) {
                    Toast.makeText(
                        context,
                        "Failed to add product with error ${error?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addProductHeart
                }
                Toast.makeText(context, "Đã thêm sản phẩm vào mục yêu thích!", Toast.LENGTH_SHORT).show()
            }
        }

        adapter.setOnCartClickedListener{ product, pos ->
            addProductCart(product.productId) { p, error ->
                if (error != null || p == null) {
                    Toast.makeText(
                        context,
                        "Failed to add product with error ${error?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addProductCart
                }
                Toast.makeText(context, "Đã thêm sản phẩm vào giỏ hàng!", Toast.LENGTH_SHORT).show()
            }
        }

        adapterT.setOnHeartClickedListener { tutorial, pos ->
            addTutorialHeart(tutorial.tutorialId) { p, error ->
                if (error != null || p == null) {
                    Toast.makeText(
                        context,
                        "Failed to add tutorial with error ${error?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addTutorialHeart
                }
                Toast.makeText(context, "Đã thêm hướng dẫn vào mục yêu thích!", Toast.LENGTH_SHORT).show()
            }
        }

        val reviews = mutableListOf<Reviews>()
        reviews.add(
            Reviews(
                R.drawable.kh1,
                "Thùy Trâm",
                "Tôi thực sự rất thích ứng dụng này, tôi đã tìm thấy rất nhiều hướng dẫn để tạo ra những đồ trang trí xinh xắn. Tuy nhiên, tôi muốn ứng dụng có thêm nhiều hướng dẫn về các sản phẩm khác nữa."
            )
        )
        reviews.add(
            Reviews(
                R.drawable.kh2,
                "Tố Uyên",
                "Ứng dụng là một nền tảng bán hàng rất tốt để bạn có thể đặt mua các sản phẩm. Tôi đã mua một số bộ trang trí thủ công khác nhau và tất cả đều được đóng gói rất cẩn thận và gửi đến nhanh chóng."
            )
        )
        reviews.add(
            Reviews(
                R.drawable.kh3,
                "Ngọc Trí",
                "Tôi đã tìm thấy rất nhiều ý tưởng tuyệt vời để tạo ra các sản phẩm handmade từ ứng dụng. Hướng dẫn rất dễ hiểu và tất cả những gì cần làm để tạo ra các sản phẩm đều được liệt kê rõ ràng."
            )
        )
        reviews.add(
            Reviews(
                R.drawable.kh4,
                "Thị Hà",
                "Tôi đã thử nhiều ứng dụng khác nhau để tìm nơi mua sản phẩm handmade, nhưng ứng dụng này là tốt nhất. Sản phẩm độc đáo, chất lượng, giá cả hợp lý. Tôi thích những hướng dẫn chi tiết của họ."
            )
        )
        reviews.add(
            Reviews(
                R.drawable.kh5,
                "Văn Cừ",
                "Ứng dụng này rất dễ sử dụng và có nhiều lựa chọn sản phẩm chất lượng. Tôi đã mua rất nhiều sản phẩm từ đây và tôi không bao giờ bị thất vọng. Tôi đánh giá cao dịch vụ giao hàng nhanh của họ."
            )
        )

        val rvReviews = view.findViewById<RecyclerView>(R.id.rv_review)
        rvReviews.layoutManager = GridLayoutManager(context, 1, RecyclerView.HORIZONTAL, false)
        rvReviews.adapter = ReviewsRecyclerAdapter(requireContext(), reviews)

        val timer = Timer()
        var currentItem = 0 // Mục hiện tại trong RecyclerView

        val timerTask = object : TimerTask() {
            override fun run() {
                // Thay đổi mục hiện tại
                currentItem++

                // Kiểm tra nếu đã đến cuối danh sách
                if (currentItem >= (rvReviews.adapter?.itemCount ?: 0)) {
                    currentItem = 0 // Quay lại vị trí đầu tiên
                }

                // Cuộn đến mục tiếp theo
                rvReviews.post {
                    rvReviews.smoothScrollToPosition(currentItem)
                }
            }
        }

        val delayInMillis: Long = 2000 // Khoảng thời gian giữa mỗi thay đổi (ví dụ: 1 giây)
        timer.scheduleAtFixedRate(timerTask, 0, delayInMillis)


        val btnViewAllProduct = view.findViewById<Button>(R.id.btn_p)
        btnViewAllProduct.setOnClickListener {
            findNavController().navigate(R.id.productFragment)
        }

        val t = view.findViewById<Button>(R.id.btn_t)
        t.setOnClickListener {
            findNavController().navigate(R.id.tutorialFragment)
        }

        val h = view.findViewById<ImageView>(R.id.img_heart)
        h.setOnClickListener {
            findNavController().navigate(R.id.heartFragment)
        }

        val c = view.findViewById<ImageView>(R.id.img_cart)
        c.setOnClickListener {
            findNavController().navigate(R.id.cartFragment)
        }

        // Navigation menu
        drawerLayout = view.findViewById(R.id.drawer_layout)
        navigationView = view.findViewById(R.id.nav_menu)

        // Button click to open menu
        val btnOpenMenu = view.findViewById<ImageView>(R.id.btn_menu)

        btnOpenMenu.setOnClickListener {
            openMenu()
        }

        // When select item on menu
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_1 -> {
                    findNavController().navigate(R.id.mainFragment)
                }

                R.id.item_2 -> {
                    findNavController().navigate(R.id.aboutFragment)
                }

                R.id.item_3 -> {
                    findNavController().navigate(R.id.productFragment)
                }

                R.id.item_4 -> {
                    findNavController().navigate(R.id.tutorialFragment)
                }

                R.id.item_5 -> {
                    findNavController().navigate(R.id.contactFragment)
                }
            }
            true
        }

    }

    private fun getProducts(callback: (List<Product>?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.getProducts("Basic ${authStorage.getToken()}")
            .enqueue(object : Callback<List<Product>> {
                override fun onResponse(
                    p0: Call<List<Product>>,
                    p1: Response<List<Product>>
                ) {
                    if (p1.isSuccessful) {
                        val tasks = p1.body()
                        callback(tasks, null)
                    } else {
                        callback(null, Throwable("Failed to get products with status ${p1.code()}"))
                    }
                }

                override fun onFailure(p0: Call<List<Product>>, p1: Throwable) {
                    p1.printStackTrace()
                    callback(null, p1)
                }

            })
        return
    }

    private fun addProductHeart(productId: String, callback: (Product?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.addProductHeart(productId,"Basic ${authStorage.getToken()}")
            .enqueue(object : Callback<Product> {
                override fun onResponse(
                    p0: Call<Product>,
                    p1: Response<Product>
                ) {
                    if (p1.isSuccessful) {
                        val task = p1.body()
                        callback(task, null)
                    } else {
                        callback(null, Throwable("Failed to add product with status ${p1.code()}"))
                    }
                }

                override fun onFailure(p0: Call<Product>, p1: Throwable) {
                    callback(null, p1)
                }

            })
        return
    }

    private fun addTutorialHeart(tutorialId: String, callback: (Tutorial?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.addTutorialHeart(tutorialId,"Basic ${authStorage.getToken()}")
            .enqueue(object : Callback<Tutorial> {
                override fun onResponse(
                    p0: Call<Tutorial>,
                    p1: Response<Tutorial>
                ) {
                    if (p1.isSuccessful) {
                        val task = p1.body()
                        callback(task, null)
                    } else {
                        callback(null, Throwable("Failed to add tutorial with status ${p1.code()}"))
                    }
                }

                override fun onFailure(p0: Call<Tutorial>, p1: Throwable) {
                    callback(null, p1)
                }

            })
        return
    }

    private fun addProductCart(productId: String, callback: (Product?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.addProductCart(productId,"Basic ${authStorage.getToken()}")
            .enqueue(object : Callback<Product> {
                override fun onResponse(
                    p0: Call<Product>,
                    p1: Response<Product>
                ) {
                    if (p1.isSuccessful) {
                        val task = p1.body()
                        callback(task, null)
                    } else {
                        callback(null, Throwable("Failed to add product with status ${p1.code()}"))
                    }
                }

                override fun onFailure(p0: Call<Product>, p1: Throwable) {
                    callback(null, p1)
                }

            })
        return
    }

    private fun getTutorials(callback: (List<Tutorial>?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.getTutorials("Basic ${authStorage.getToken()}")
            .enqueue(object : Callback<List<Tutorial>> {
                override fun onResponse(
                    p0: Call<List<Tutorial>>,
                    p1: Response<List<Tutorial>>
                ) {
                    if (p1.isSuccessful) {
                        val tasks = p1.body()
                        callback(tasks, null)
                    } else {
                        callback(null, Throwable("Failed to get tutorials with status ${p1.code()}"))
                    }
                }

                override fun onFailure(p0: Call<List<Tutorial>>, p1: Throwable) {
                    p1.printStackTrace()
                    callback(null, p1)
                }

            })
        return
    }

    // Navigation menu
    fun openMenu() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    fun closeMenu() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onResume() {
        super.onResume()
        // Đảm bảo NestedScrollView cuộn về đầu khi Fragment được hiển thị lại
        view?.findViewById<NestedScrollView>(R.id.nestedScrollView)?.scrollTo(0, 0)
    }
}