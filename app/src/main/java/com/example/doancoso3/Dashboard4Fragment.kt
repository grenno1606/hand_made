package com.example.doancoso3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs3_ns_22ns082.Reviews
import com.example.dacs3_ns_22ns082.ReviewsRecyclerAdapter

class Dashboard4Fragment : IsAdminFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val reviews= mutableListOf<Reviews>()
        reviews.add(Reviews(R.drawable.kh1,"Thùy Trâm","Tôi thực sự rất thích ứng dụng này, tôi đã tìm thấy rất nhiều hướng dẫn để tạo ra những đồ trang trí xinh xắn. Tuy nhiên, tôi muốn ứng dụng có thêm nhiều hướng dẫn về các sản phẩm khác nữa."))
        reviews.add(Reviews(R.drawable.kh2,"Tố Uyên","Ứng dụng là một nền tảng bán hàng rất tốt để bạn có thể đặt mua các sản phẩm. Tôi đã mua một số bộ trang trí thủ công khác nhau và tất cả đều được đóng gói rất cẩn thận và gửi đến nhanh chóng."))
        reviews.add(Reviews(R.drawable.kh3,"Ngọc Trí","Tôi đã tìm thấy rất nhiều ý tưởng tuyệt vời để tạo ra các sản phẩm handmade từ ứng dụng. Hướng dẫn rất dễ hiểu và tất cả những gì cần làm để tạo ra các sản phẩm đều được liệt kê một cách rõ ràng."))
        reviews.add(Reviews(R.drawable.kh4,"Thị Hà","Tôi đã thử nhiều ứng dụng khác nhau để tìm nơi mua sản phẩm handmade, nhưng ứng dụng này là tốt nhất. Sản phẩm của họ độc đáo và chất lượng, còn giá cả rất hợp lý. Tôi cũng thích những hướng dẫn chi tiết của họ."))
        reviews.add(Reviews(R.drawable.kh5,"Văn Cừ","Ứng dụng này rất dễ sử dụng và có nhiều lựa chọn sản phẩm chất lượng. Tôi đã mua rất nhiều sản phẩm từ đây và tôi không bao giờ bị thất vọng. Tôi cũng đánh giá rất cao dịch vụ giao hàng nhanh chóng của họ."))
        val rvReviews=view.findViewById<RecyclerView>(R.id.rv_review)
        rvReviews.layoutManager = GridLayoutManager(context, 1, RecyclerView.VERTICAL, false)
        rvReviews.adapter= ReviewsRecyclerAdapter(requireContext(),reviews)
    }

}