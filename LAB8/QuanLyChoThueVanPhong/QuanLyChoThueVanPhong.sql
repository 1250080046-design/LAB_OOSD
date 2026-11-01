/* BÀI 4 - Quản lý cho thuê văn phòng cao ốc; SQL Server 2017+. Chạy trong CSDL THỬ NGHIỆM MỚI.
   Dữ liệu biểu phí/phạt chỉ là minh họa; không phải quy định pháp lý/thực tế. */
IF DB_ID(N'QuanLyChoThueVanPhong') IS NULL CREATE DATABASE QuanLyChoThueVanPhong;
GO
USE QuanLyChoThueVanPhong;
GO
IF OBJECT_ID('dbo.Tang','U') IS NOT NULL THROW 50001,N'CSDL đã có bảng. Không chạy lại script tạo mẫu trên dữ liệu đang dùng.',1;
CREATE TABLE Tang(MaTang INT IDENTITY PRIMARY KEY, TenTang NVARCHAR(80) NOT NULL UNIQUE, DoCao INT NOT NULL CHECK(DoCao>=0));
CREATE TABLE VanPhong(MaPhong INT IDENTITY PRIMARY KEY, MaTang INT NOT NULL REFERENCES Tang(MaTang), SoPhong NVARCHAR(30) NOT NULL UNIQUE, DienTich DECIMAL(10,2) NOT NULL CHECK(DienTich>0), Huong NVARCHAR(30) NOT NULL, DonGiaThang DECIMAL(18,2) NOT NULL CHECK(DonGiaThang>0), TrangThai NVARCHAR(20) NOT NULL DEFAULT N'TRONG' CHECK(TrangThai IN(N'TRONG',N'DANG_THUE',N'BAO_TRI')));
CREATE TABLE KhachHang(MaKH INT IDENTITY PRIMARY KEY, TenCongTy NVARCHAR(200) NOT NULL, MaSoThue NVARCHAR(30) NOT NULL UNIQUE, DaiDien NVARCHAR(120) NOT NULL, DienThoai VARCHAR(20) NOT NULL, Email VARCHAR(120) NULL, DiaChi NVARCHAR(300) NULL);
CREATE TABLE HopDong(MaHD INT IDENTITY PRIMARY KEY, SoHopDong NVARCHAR(40) NOT NULL UNIQUE, MaKH INT NOT NULL REFERENCES KhachHang(MaKH), NgayKy DATE NOT NULL, NgayBatDau DATE NOT NULL, SoThang INT NOT NULL CHECK(SoThang>=6), NgayKetThuc DATE NOT NULL, TrangThai NVARCHAR(20) NOT NULL DEFAULT N'HIEU_LUC' CHECK(TrangThai IN(N'HIEU_LUC',N'HET_HAN',N'DA_HUY')), GhiChu NVARCHAR(500) NULL);
CREATE TABLE ChiTietHopDong(MaCTHD INT IDENTITY PRIMARY KEY, MaHD INT NOT NULL REFERENCES HopDong(MaHD), MaPhong INT NOT NULL REFERENCES VanPhong(MaPhong), GiaThueThang DECIMAL(18,2) NOT NULL CHECK(GiaThueThang>0), UNIQUE(MaHD,MaPhong));
CREATE TABLE HoaDon(MaHoaDon INT IDENTITY PRIMARY KEY, MaHD INT NOT NULL REFERENCES HopDong(MaHD), Loai NVARCHAR(20) NOT NULL CHECK(Loai IN(N'THUE',N'DICH_VU',N'PHAT')), KyNam INT NOT NULL CHECK(KyNam BETWEEN 2000 AND 2100), KyThang INT NOT NULL CHECK(KyThang BETWEEN 1 AND 12), TuThang INT NULL, DenThang INT NULL, NgayLap DATE NOT NULL, HanThanhToan DATE NOT NULL, TongTien DECIMAL(18,2) NOT NULL CHECK(TongTien>=0), GhiChu NVARCHAR(400) NULL, CHECK((Loai=N'THUE' AND TuThang IS NOT NULL AND DenThang IS NOT NULL AND TuThang>=1 AND DenThang>=TuThang) OR (Loai<>N'THUE' AND TuThang IS NULL AND DenThang IS NULL)));
CREATE UNIQUE INDEX UX_HoaDon_ThueKy ON HoaDon(MaHD,Loai,TuThang,DenThang) WHERE Loai=N'THUE';
CREATE UNIQUE INDEX UX_HoaDon_DichVuKy ON HoaDon(MaHD,Loai,KyNam,KyThang) WHERE Loai=N'DICH_VU';
CREATE TABLE ThanhToan(MaThanhToan INT IDENTITY PRIMARY KEY, MaHoaDon INT NOT NULL REFERENCES HoaDon(MaHoaDon), NgayThanhToan DATE NOT NULL, SoTien DECIMAL(18,2) NOT NULL CHECK(SoTien>0), HinhThuc NVARCHAR(30) NOT NULL CHECK(HinhThuc IN(N'TIEN_MAT',N'CHUYEN_KHOAN')), MaThamChieu NVARCHAR(80) NULL);
CREATE UNIQUE INDEX UX_ThanhToan_MaThamChieu ON ThanhToan(MaThamChieu) WHERE MaThamChieu IS NOT NULL;
CREATE TABLE DichVu(MaDV INT IDENTITY PRIMARY KEY, TenDV NVARCHAR(100) NOT NULL UNIQUE, DonVi NVARCHAR(30) NOT NULL, DonGia DECIMAL(18,2) NOT NULL CHECK(DonGia>=0));
CREATE TABLE SuDungDichVu(MaSuDung INT IDENTITY PRIMARY KEY, MaHD INT NOT NULL REFERENCES HopDong(MaHD), MaPhong INT NOT NULL REFERENCES VanPhong(MaPhong), MaDV INT NOT NULL REFERENCES DichVu(MaDV), KyNam INT NOT NULL CHECK(KyNam BETWEEN 2000 AND 2100), KyThang INT NOT NULL CHECK(KyThang BETWEEN 1 AND 12), SoLuong DECIMAL(12,2) NOT NULL CHECK(SoLuong>=0), DonGia DECIMAL(18,2) NOT NULL CHECK(DonGia>=0), UNIQUE(MaHD,MaPhong,MaDV,KyNam,KyThang));
CREATE TABLE GiaHan(MaGiaHan INT IDENTITY PRIMARY KEY, MaHD INT NOT NULL REFERENCES HopDong(MaHD), MaPhong INT NOT NULL REFERENCES VanPhong(MaPhong), NgayLap DATE NOT NULL, SoThangGiaHan INT NOT NULL CHECK(SoThangGiaHan>0), TuNgay DATE NOT NULL, DenNgay DATE NOT NULL, GhiChu NVARCHAR(300) NULL);
CREATE TABLE NhanVienVanPhong(MaNV INT IDENTITY PRIMARY KEY, MaKH INT NOT NULL REFERENCES KhachHang(MaKH), MaPhong INT NOT NULL REFERENCES VanPhong(MaPhong), HoTen NVARCHAR(120) NOT NULL, ChucVu NVARCHAR(80) NOT NULL, HinhAnh NVARCHAR(350) NULL, DienThoai VARCHAR(20) NULL, DangLam BIT NOT NULL DEFAULT 1);
CREATE TABLE TheGiuXe(MaThe INT IDENTITY PRIMARY KEY, SoThe NVARCHAR(30) NOT NULL UNIQUE, MaNV INT NOT NULL REFERENCES NhanVienVanPhong(MaNV), BienSo NVARCHAR(30) NOT NULL, LoaiXe NVARCHAR(50) NOT NULL, NgayCap DATE NOT NULL, TrangThai NVARCHAR(20) NOT NULL DEFAULT N'HIEU_LUC' CHECK(TrangThai IN(N'HIEU_LUC',N'THU_HOI')));
CREATE TABLE KiemTraPhong(MaKiemTra INT IDENTITY PRIMARY KEY, MaPhong INT NOT NULL REFERENCES VanPhong(MaPhong), NgayKiemTra DATE NOT NULL, TinhTrang NVARCHAR(200) NOT NULL, TuYSuaDoi BIT NOT NULL DEFAULT 0, SoTienPhat DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK(SoTienPhat>=0), GhiChu NVARCHAR(500) NULL);
CREATE INDEX IX_CTHD_Phong ON ChiTietHopDong(MaPhong,MaHD);
CREATE INDEX IX_HoaDon_HD ON HoaDon(MaHD,Loai,KyNam,KyThang);
CREATE INDEX IX_SDDV_Ky ON SuDungDichVu(MaHD,KyNam,KyThang);
CREATE INDEX IX_KiemTraPhong_Ngay ON KiemTraPhong(MaPhong,NgayKiemTra DESC);
GO
CREATE OR ALTER PROCEDURE dbo.sp_TaoHopDong
 @SoHopDong NVARCHAR(40), @MaKH INT, @NgayKy DATE, @NgayBatDau DATE, @SoThang INT,
 @DanhSachPhong NVARCHAR(MAX), @HinhThuc NVARCHAR(30)=N'CHUYEN_KHOAN', @MaHDMoi INT OUTPUT
AS
BEGIN
 SET NOCOUNT ON; SET XACT_ABORT ON;
 IF @SoThang<6 OR @SoThang>120 THROW 51000,N'Thời gian thuê phải ít nhất 6 tháng (tối đa 120 tháng trong prototype).',1;
 IF @DanhSachPhong IS NULL OR LTRIM(RTRIM(@DanhSachPhong))='' THROW 51001,N'Chọn ít nhất một văn phòng.',1;
 IF @HinhThuc NOT IN(N'TIEN_MAT',N'CHUYEN_KHOAN') THROW 51002,N'Hình thức thanh toán không hợp lệ.',1;
 DECLARE @KetThuc DATE=DATEADD(day,-1,DATEADD(month,@SoThang,@NgayBatDau));
 DECLARE @Phong TABLE (MaPhong INT PRIMARY KEY);
 INSERT INTO @Phong SELECT DISTINCT TRY_CAST(value AS INT) FROM STRING_SPLIT(@DanhSachPhong,',') WHERE TRY_CAST(value AS INT) IS NOT NULL;
 IF NOT EXISTS(SELECT 1 FROM @Phong) THROW 51003,N'Danh sách phòng không hợp lệ.',1;
 BEGIN TRY
 BEGIN TRAN;
 IF EXISTS(SELECT 1 FROM @Phong p LEFT JOIN VanPhong v WITH(UPDLOCK,HOLDLOCK) ON p.MaPhong=v.MaPhong WHERE v.MaPhong IS NULL OR v.TrangThai=N'BAO_TRI') THROW 51004,N'Phòng không tồn tại hoặc đang bảo trì.',1;
 IF EXISTS(SELECT 1 FROM @Phong p JOIN ChiTietHopDong c WITH(UPDLOCK,HOLDLOCK) ON c.MaPhong=p.MaPhong JOIN HopDong h WITH(UPDLOCK,HOLDLOCK) ON h.MaHD=c.MaHD WHERE h.TrangThai=N'HIEU_LUC' AND h.NgayBatDau<=@KetThuc AND h.NgayKetThuc>=@NgayBatDau) THROW 51005,N'Có phòng bị trùng lịch thuê.',1;
 INSERT HopDong(SoHopDong,MaKH,NgayKy,NgayBatDau,SoThang,NgayKetThuc) VALUES(@SoHopDong,@MaKH,@NgayKy,@NgayBatDau,@SoThang,@KetThuc);
 SET @MaHDMoi=SCOPE_IDENTITY();
 INSERT ChiTietHopDong(MaHD,MaPhong,GiaThueThang) SELECT @MaHDMoi,v.MaPhong,v.DonGiaThang FROM VanPhong v JOIN @Phong p ON v.MaPhong=p.MaPhong;
 DECLARE @Tien DECIMAL(18,2)=(SELECT SUM(GiaThueThang)*6 FROM ChiTietHopDong WHERE MaHD=@MaHDMoi);
 INSERT HoaDon(MaHD,Loai,KyNam,KyThang,TuThang,DenThang,NgayLap,HanThanhToan,TongTien,GhiChu)
 VALUES(@MaHDMoi,N'THUE',YEAR(@NgayBatDau),MONTH(@NgayBatDau),1,6,@NgayKy,@NgayKy,@Tien,N'Thanh toán trước 6 tháng đầu');
 DECLARE @HD INT=SCOPE_IDENTITY();
 INSERT ThanhToan(MaHoaDon,NgayThanhToan,SoTien,HinhThuc) VALUES(@HD,@NgayKy,@Tien,@HinhThuc);
 UPDATE v SET TrangThai=N'DANG_THUE' FROM VanPhong v JOIN @Phong p ON v.MaPhong=p.MaPhong;
 COMMIT;
 END TRY BEGIN CATCH IF @@TRANCOUNT>0 ROLLBACK; THROW; END CATCH
END;
GO
CREATE OR ALTER PROCEDURE dbo.sp_LapHoaDonThueThang
 @MaHD INT,@ThuTuThang INT,@NgayLap DATE,@MaHoaDonMoi INT OUTPUT
AS
BEGIN
 SET NOCOUNT ON;SET XACT_ABORT ON;
 IF @ThuTuThang<7 THROW 51010,N'6 tháng đầu đã thanh toán khi ký hợp đồng.',1;
 DECLARE @BatDau DATE,@SoThang INT;
 SELECT @BatDau=NgayBatDau,@SoThang=SoThang FROM HopDong WHERE MaHD=@MaHD AND TrangThai=N'HIEU_LUC';
 IF @BatDau IS NULL THROW 51011,N'Hợp đồng không hợp lệ.',1;
 IF @ThuTuThang>@SoThang+(SELECT ISNULL(SUM(SoThangGiaHan),0) FROM GiaHan WHERE MaHD=@MaHD) THROW 51012,N'Tháng này vượt thời gian thuê.',1;
 DECLARE @DauThang DATE=DATEADD(month,@ThuTuThang-1,@BatDau);
 IF @NgayLap>@DauThang THROW 51013,N'Tiền thuê từ tháng thứ 7 phải lập và thu vào đầu kỳ.',1;
 DECLARE @Tong DECIMAL(18,2)=(SELECT SUM(GiaThueThang) FROM ChiTietHopDong WHERE MaHD=@MaHD);
 BEGIN TRY
 BEGIN TRAN;
 INSERT HoaDon(MaHD,Loai,KyNam,KyThang,TuThang,DenThang,NgayLap,HanThanhToan,TongTien)
 VALUES(@MaHD,N'THUE',YEAR(@DauThang),MONTH(@DauThang),@ThuTuThang,@ThuTuThang,@NgayLap,@DauThang,@Tong);
 SET @MaHoaDonMoi=SCOPE_IDENTITY();
 COMMIT;
 END TRY BEGIN CATCH IF @@TRANCOUNT>0 ROLLBACK;THROW;END CATCH
END;
GO
CREATE OR ALTER PROCEDURE dbo.sp_LapHoaDonDichVu
 @MaHD INT,@Nam INT,@Thang INT,@NgayLap DATE,@MaHoaDonMoi INT OUTPUT
AS
BEGIN
 SET NOCOUNT ON;SET XACT_ABORT ON;
 IF @Thang<1 OR @Thang>12 THROW 51020,N'Tháng không hợp lệ.',1;
 IF @NgayLap<EOMONTH(DATEFROMPARTS(@Nam,@Thang,1)) THROW 51021,N'Dịch vụ chỉ chốt hóa đơn cuối tháng.',1;
 DECLARE @Tong DECIMAL(18,2)=(SELECT ISNULL(SUM(s.SoLuong*s.DonGia),0) FROM SuDungDichVu s JOIN ChiTietHopDong c ON c.MaHD=s.MaHD AND c.MaPhong=s.MaPhong WHERE s.MaHD=@MaHD AND s.KyNam=@Nam AND s.KyThang=@Thang);
 IF @Tong<=0 THROW 51022,N'Không có dịch vụ phát sinh trong kỳ.',1;
 BEGIN TRY BEGIN TRAN;
 INSERT HoaDon(MaHD,Loai,KyNam,KyThang,NgayLap,HanThanhToan,TongTien) VALUES(@MaHD,N'DICH_VU',@Nam,@Thang,@NgayLap,@NgayLap,@Tong);
 SET @MaHoaDonMoi=SCOPE_IDENTITY(); COMMIT;
 END TRY BEGIN CATCH IF @@TRANCOUNT>0 ROLLBACK;THROW;END CATCH
END;
GO
CREATE OR ALTER PROCEDURE dbo.sp_ThuTien
 @MaHoaDon INT,@SoTien DECIMAL(18,2),@NgayThu DATE,@HinhThuc NVARCHAR(30),@MaThamChieu NVARCHAR(80)=NULL
AS
BEGIN
 SET NOCOUNT ON;SET XACT_ABORT ON;
 IF @SoTien<=0 THROW 51030,N'Số tiền phải lớn hơn 0.',1;
 IF @HinhThuc NOT IN(N'TIEN_MAT',N'CHUYEN_KHOAN') THROW 51031,N'Hình thức không hợp lệ.',1;
 BEGIN TRY BEGIN TRAN;
 DECLARE @Tong DECIMAL(18,2);
 SELECT @Tong=TongTien FROM HoaDon WITH(UPDLOCK,HOLDLOCK) WHERE MaHoaDon=@MaHoaDon;
 IF @Tong IS NULL THROW 51032,N'Hóa đơn không tồn tại.',1;
 IF @SoTien+ISNULL((SELECT SUM(SoTien) FROM ThanhToan WHERE MaHoaDon=@MaHoaDon),0)>@Tong THROW 51033,N'Không được thu vượt công nợ.',1;
 INSERT ThanhToan(MaHoaDon,NgayThanhToan,SoTien,HinhThuc,MaThamChieu) VALUES(@MaHoaDon,@NgayThu,@SoTien,@HinhThuc,@MaThamChieu);
 COMMIT; END TRY BEGIN CATCH IF @@TRANCOUNT>0 ROLLBACK;THROW;END CATCH
END;
GO
CREATE OR ALTER PROCEDURE dbo.sp_GiaHan
 @MaHD INT,@MaPhong INT,@SoThangGiaHan INT,@NgayLap DATE,@MaGiaHanMoi INT OUTPUT
AS
BEGIN
 SET NOCOUNT ON;SET XACT_ABORT ON;
 BEGIN TRY BEGIN TRAN;
 DECLARE @ThangGoc INT,@NgayKetThuc DATE;
 SELECT @ThangGoc=SoThang,@NgayKetThuc=NgayKetThuc FROM HopDong WITH(UPDLOCK,HOLDLOCK) WHERE MaHD=@MaHD AND TrangThai=N'HIEU_LUC';
 IF @ThangGoc IS NULL THROW 51040,N'Hợp đồng không hợp lệ.',1;
 IF NOT EXISTS(SELECT 1 FROM ChiTietHopDong WHERE MaHD=@MaHD AND MaPhong=@MaPhong) THROW 51041,N'Phòng không thuộc hợp đồng.',1;
 IF (SELECT COUNT(*) FROM ChiTietHopDong WHERE MaHD=@MaHD)<>1 THROW 51047,N'Gia hạn trên prototype yêu cầu hợp đồng chỉ có một phòng; hợp đồng nhiều phòng cần ký lại để tránh kéo dài phòng khác.',1;
 IF @SoThangGiaHan<1 OR @SoThangGiaHan>@ThangGoc/2 THROW 51042,N'Mỗi lần gia hạn không vượt quá 1/2 thời gian thuê gốc.',1;
 IF EXISTS(SELECT 1 FROM HoaDon h WHERE h.MaHD=@MaHD AND h.TongTien>ISNULL((SELECT SUM(t.SoTien) FROM ThanhToan t WHERE t.MaHoaDon=h.MaHoaDon),0)) THROW 51043,N'Không thể gia hạn khi còn nợ tiền thuê/dịch vụ/phạt.',1;
 IF EXISTS(SELECT 1 FROM SuDungDichVu s WHERE s.MaHD=@MaHD AND NOT EXISTS(SELECT 1 FROM HoaDon h WHERE h.MaHD=s.MaHD AND h.Loai=N'DICH_VU' AND h.KyNam=s.KyNam AND h.KyThang=s.KyThang) AND EOMONTH(DATEFROMPARTS(s.KyNam,s.KyThang,1))<=@NgayLap) THROW 51044,N'Có dịch vụ đã đến kỳ nhưng chưa lập hóa đơn.',1;
 DECLARE @NgayBatDauGiaHan DATE=DATEADD(day,1,@NgayKetThuc);
 DECLARE @NgayKetThucMoi DATE=DATEADD(day,-1,DATEADD(month,@SoThangGiaHan,@NgayBatDauGiaHan));
 IF EXISTS(SELECT 1 FROM GiaHan WHERE MaHD=@MaHD AND MaPhong=@MaPhong) THROW 51045,N'Prototype giới hạn mỗi phòng một phiếu gia hạn; vui lòng lập hợp đồng mới nếu cần.',1;
 IF EXISTS(SELECT 1 FROM ChiTietHopDong c JOIN HopDong h ON c.MaHD=h.MaHD WHERE c.MaPhong=@MaPhong AND c.MaHD<>@MaHD AND h.TrangThai=N'HIEU_LUC' AND h.NgayBatDau<=@NgayKetThucMoi AND h.NgayKetThuc>=@NgayBatDauGiaHan) THROW 51046,N'Vướng lịch thuê khác.',1;
 INSERT GiaHan(MaHD,MaPhong,NgayLap,SoThangGiaHan,TuNgay,DenNgay) VALUES(@MaHD,@MaPhong,@NgayLap,@SoThangGiaHan,@NgayBatDauGiaHan,@NgayKetThucMoi);
 SET @MaGiaHanMoi=SCOPE_IDENTITY();
 UPDATE HopDong SET NgayKetThuc=CASE WHEN NgayKetThuc<@NgayKetThucMoi THEN @NgayKetThucMoi ELSE NgayKetThuc END WHERE MaHD=@MaHD;
 COMMIT; END TRY BEGIN CATCH IF @@TRANCOUNT>0 ROLLBACK;THROW;END CATCH
END;
GO
INSERT Tang(TenTang,DoCao) SELECT CONCAT(N'Tầng ',v.n),v.n FROM (VALUES(1),(2),(3),(4),(5),(6),(7),(8))v(n);
;WITH n AS(SELECT 1 i UNION ALL SELECT i+1 FROM n WHERE i<48)
INSERT VanPhong(MaTang,SoPhong,DienTich,Huong,DonGiaThang)
SELECT (i-1)/6+1,CONCAT(N'P',100+((i-1)/6+1)*100+((i-1)%6+1)),45+((i*17)%110),CHOOSE((i%4)+1,N'Đông',N'Tây',N'Nam',N'Bắc'),5000000+((i-1)/6)*800000+((i*11)%8)*600000 FROM n OPTION(MAXRECURSION 0);
;WITH n AS(SELECT 1 i UNION ALL SELECT i+1 FROM n WHERE i<12)
INSERT KhachHang(TenCongTy,MaSoThue,DaiDien,DienThoai,Email,DiaChi)
SELECT CONCAT(N'Công ty mẫu ',RIGHT('00'+CAST(i AS VARCHAR(2)),2)),CONCAT('MST-DEMO-',RIGHT('000'+CAST(i AS VARCHAR(3)),3)),CONCAT(N'Đại diện ',i),CONCAT('0908',RIGHT('000000'+CAST(i*983 AS VARCHAR(6)),6)),CONCAT('contact',i,'@example.test'),N'Địa chỉ minh họa' FROM n OPTION(MAXRECURSION 0);
INSERT DichVu(TenDV,DonVi,DonGia) VALUES(N'Điện',N'kWh',3200),(N'Nước',N'm3',16000),(N'Internet',N'tháng',450000),(N'Vệ sinh',N'tháng',250000),(N'Bảo trì điều hòa',N'lần',350000);
DECLARE @i INT=1,@new INT;
WHILE @i<=10 BEGIN
 DECLARE @code NVARCHAR(40)=CONCAT(N'HD-DEMO-',RIGHT('000'+CAST(@i AS VARCHAR(3)),3));
 DECLARE @rooms NVARCHAR(MAX)=CONVERT(NVARCHAR(10),@i);
 EXEC dbo.sp_TaoHopDong @SoHopDong=@code,@MaKH=@i,@NgayKy='2026-01-01',@NgayBatDau='2026-01-01',@SoThang=12,@DanhSachPhong=@rooms,@MaHDMoi=@new OUTPUT;
 SET @i+=1;
END;
;WITH n AS(SELECT 1 i UNION ALL SELECT i+1 FROM n WHERE i<80)
INSERT NhanVienVanPhong(MaKH,MaPhong,HoTen,ChucVu,HinhAnh,DienThoai)
SELECT ((i-1)%10)+1,((i-1)%10)+1,CHOOSE((i%10)+1,N'Nguyễn Minh Anh',N'Trần Quốc Bảo',N'Lê Thanh Bình',N'Phạm Ngọc Châu',N'Võ Gia Hân',N'Đặng Thành Công',N'Bùi Thảo Linh',N'Hoàng Minh Đức',N'Đỗ Hải Yến',N'Phan Nhật Nam'),CHOOSE((i%4)+1,N'Nhân viên',N'Trưởng nhóm',N'Kỹ thuật',N'Kế toán'),CONCAT(N'images/nhanvien_',i,'.jpg'),CONCAT('0912',RIGHT('000000'+CAST(i*729 AS VARCHAR(6)),6)) FROM n OPTION(MAXRECURSION 0);
;WITH n AS(SELECT 1 i UNION ALL SELECT i+1 FROM n WHERE i<60)
INSERT TheGiuXe(SoThe,MaNV,BienSo,LoaiXe,NgayCap)
SELECT CONCAT(N'TXE-',RIGHT('0000'+CAST(i AS VARCHAR(4)),4)),i,CONCAT('59-X',i%10,'-',RIGHT('00000'+CAST(i*113 AS VARCHAR(5)),5)),CASE WHEN i%4=0 THEN N'Ô tô' ELSE N'Xe máy' END,'2026-01-05' FROM n OPTION(MAXRECURSION 0);
;WITH n AS(SELECT 1 i UNION ALL SELECT i+1 FROM n WHERE i<48)
INSERT KiemTraPhong(MaPhong,NgayKiemTra,TinhTrang,TuYSuaDoi,SoTienPhat,GhiChu)
SELECT i,'2026-03-15',CASE WHEN i%9=0 THEN N'Cần sơn lại' ELSE N'Bình thường' END,CASE WHEN i%17=0 THEN 1 ELSE 0 END,CASE WHEN i%17=0 THEN 1500000 ELSE 0 END,N'Kiểm tra định kỳ mẫu' FROM n OPTION(MAXRECURSION 0);
INSERT SuDungDichVu(MaHD,MaPhong,MaDV,KyNam,KyThang,SoLuong,DonGia)
SELECT h.MaHD,c.MaPhong,d.MaDV,2026,m.Thang,CASE d.MaDV WHEN 1 THEN 180 WHEN 2 THEN 25 ELSE 1 END,d.DonGia
FROM HopDong h JOIN ChiTietHopDong c ON h.MaHD=c.MaHD CROSS JOIN DichVu d CROSS JOIN(VALUES(1),(2),(3))m(Thang) WHERE d.MaDV<=4;
DECLARE @hd INT=1,@th INT=1,@bill INT;
WHILE @hd<=10 BEGIN SET @th=1;WHILE @th<=3 BEGIN
 DECLARE @billDate DATE=CASE @th WHEN 1 THEN '2026-01-31' WHEN 2 THEN '2026-02-28' ELSE '2026-03-31' END;
 EXEC dbo.sp_LapHoaDonDichVu @MaHD=@hd,@Nam=2026,@Thang=@th,@NgayLap=@billDate,@MaHoaDonMoi=@bill OUTPUT;
 IF @th<=2 BEGIN DECLARE @sum DECIMAL(18,2)=(SELECT TongTien FROM HoaDon WHERE MaHoaDon=@bill); DECLARE @datePay DATE=CASE @th WHEN 1 THEN '2026-01-31' ELSE '2026-02-28' END; EXEC dbo.sp_ThuTien @MaHoaDon=@bill,@SoTien=@sum,@NgayThu=@datePay,@HinhThuc=N'CHUYEN_KHOAN'; END;
 SET @th+=1;END;SET @hd+=1;END;
EXEC dbo.sp_LapHoaDonThueThang @MaHD=1,@ThuTuThang=7,@NgayLap='2026-07-01',@MaHoaDonMoi=@bill OUTPUT;
DECLARE @rent DECIMAL(18,2)=(SELECT TongTien FROM HoaDon WHERE MaHoaDon=@bill);
EXEC dbo.sp_ThuTien @MaHoaDon=@bill,@SoTien=@rent,@NgayThu='2026-07-01',@HinhThuc=N'CHUYEN_KHOAN';
GO
SELECT N'Tang' Bang,COUNT(*) SoDong FROM Tang UNION ALL SELECT N'VanPhong',COUNT(*) FROM VanPhong UNION ALL SELECT N'KhachHang',COUNT(*) FROM KhachHang UNION ALL SELECT N'HopDong',COUNT(*) FROM HopDong UNION ALL SELECT N'ChiTietHopDong',COUNT(*) FROM ChiTietHopDong UNION ALL SELECT N'HoaDon',COUNT(*) FROM HoaDon UNION ALL SELECT N'ThanhToan',COUNT(*) FROM ThanhToan UNION ALL SELECT N'DichVu',COUNT(*) FROM DichVu UNION ALL SELECT N'SuDungDichVu',COUNT(*) FROM SuDungDichVu UNION ALL SELECT N'GiaHan',COUNT(*) FROM GiaHan UNION ALL SELECT N'NhanVienVanPhong',COUNT(*) FROM NhanVienVanPhong UNION ALL SELECT N'TheGiuXe',COUNT(*) FROM TheGiuXe UNION ALL SELECT N'KiemTraPhong',COUNT(*) FROM KiemTraPhong;
