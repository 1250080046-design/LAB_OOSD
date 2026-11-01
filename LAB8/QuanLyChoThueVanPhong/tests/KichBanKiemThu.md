# Kịch bản kiểm thử Bài 4 – Quản lý cho thuê văn phòng

**Trạng thái hiện tại:** Chưa chạy tích hợp với SQL Server. Tất cả ca nghiệp vụ bên dưới là **dữ liệu và kết quả mong đợi**, không phải kết quả đã đạt.

| ID | Dữ liệu/Thao tác | Kết quả mong đợi | Trạng thái |
|---|---|---|---|
| TC01 | Tạo phòng `P-DEMO`, trùng `SoPhong` đã có | SQL từ chối UNIQUE | Chưa chạy |
| TC02 | Ký hợp đồng 5 tháng | Từ chối BR02 | Chưa chạy |
| TC03 | Ký hợp đồng 6 tháng, phòng trống | Tạo HĐ + chi tiết + hóa đơn 6 tháng + thanh toán cùng transaction | Chưa chạy |
| TC04 | Ký HĐ chọn 2 phòng trống | Có 2 ChiTietHopDong, thu 6 tháng tổng của 2 phòng | Chưa chạy |
| TC05 | Ký HĐ chọn phòng đã thuê cùng kỳ | Từ chối trùng lịch; không lưu một phần | Chưa chạy |
| TC06 | Ký hợp đồng nhiều phòng có 1 phòng không tồn tại | Từ chối và rollback | Chưa chạy |
| TC07 | Lập hóa đơn thuê tháng thứ 6 | Từ chối vì đã trả trước | Chưa chạy |
| TC08 | Lập hóa đơn tháng thứ 7 ngày đầu kỳ | Lập HĐ thuê tháng 7 đúng số tiền | Chưa chạy |
| TC09 | Lập hóa đơn thuê tháng 7 sau ngày bắt đầu kỳ | Từ chối lập trễ | Chưa chạy |
| TC10 | Chốt hóa đơn dịch vụ ngày 15 trong tháng | Từ chối vì chưa cuối tháng | Chưa chạy |
| TC11 | Chốt dịch vụ sau ngày cuối tháng với phát sinh | Lập hóa đơn đúng sum(SoLuong * DonGia) | Chưa chạy |
| TC12 | Thanh toán vượt TongTien - DaThu | Từ chối, không tăng số đã thu | Chưa chạy |
| TC13 | Nhập ngày `30/10/2026` thay `2026-10-30` | Hiện lỗi, giữ nguyên Form và ô khác | Chưa chạy |
| TC14 | Gia hạn HĐ một phòng thời gian gốc 12 tháng, thêm 7 | Từ chối vì > 6 tháng | Chưa chạy |
| TC15 | Gia hạn hợp đồng còn nợ hóa đơn dịch vụ tháng 3 | Từ chối | Chưa chạy |
| TC16 | Gia hạn khi dịch vụ đã đến kỳ nhưng chưa lập hóa đơn | Từ chối | Chưa chạy |
| TC17 | Gia hạn HĐ 1 phòng đã thanh toán hết, thêm 3 tháng | Tạo phiếu gia hạn và cập nhật kỳ | Chưa chạy |
| TC18 | Thẻ xe trùng SoThe, hoặc nhân viên không tồn tại | Từ chối UNIQUE/FK | Chưa chạy |

## Kiểm tra bằng SQL

```sql
USE QuanLyChoThueVanPhong;
SELECT h.MaHD, h.SoHopDong, hd.MaHoaDon, hd.Loai, hd.TongTien,
       ISNULL(SUM(t.SoTien),0) AS DaThu,
       hd.TongTien-ISNULL(SUM(t.SoTien),0) AS ConNo
FROM HopDong h
JOIN HoaDon hd ON h.MaHD=hd.MaHD
LEFT JOIN ThanhToan t ON hd.MaHoaDon=t.MaHoaDon
GROUP BY h.MaHD,h.SoHopDong,hd.MaHoaDon,hd.Loai,hd.TongTien
ORDER BY h.MaHD,hd.MaHoaDon;
```

Lưu ý: Dữ liệu mẫu cố ý có hóa đơn dịch vụ tháng 3 chưa thanh toán để kiểm tra tình huống bị từ chối gia hạn. Hãy sao lưu CSDL trước khi thử xóa hoặc cập nhật dữ liệu.
