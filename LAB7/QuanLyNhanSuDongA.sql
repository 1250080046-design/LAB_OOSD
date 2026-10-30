IF DB_ID(N'QuanLyNhanSuDongA') IS NULL CREATE DATABASE QuanLyNhanSuDongA;
GO
USE QuanLyNhanSuDongA;
GO
CREATE TABLE PhongBan (MaPhong INT IDENTITY PRIMARY KEY, TenPhong NVARCHAR(120) NOT NULL UNIQUE, LoaiDonVi NVARCHAR(30) NOT NULL DEFAULT N'PHONG', TrangThai BIT NOT NULL DEFAULT 1);
CREATE TABLE ChucVu (MaChucVu INT IDENTITY PRIMARY KEY, TenChucVu NVARCHAR(100) NOT NULL UNIQUE);
CREATE TABLE NhanVien (MaNV INT IDENTITY PRIMARY KEY, HoTen NVARCHAR(120) NOT NULL, NgaySinh DATE NULL, DienThoai VARCHAR(20) NULL, DiaChi NVARCHAR(250) NULL, NgayVaoLam DATE NOT NULL, MaPhong INT NOT NULL REFERENCES PhongBan(MaPhong), TrangThai VARCHAR(20) NOT NULL DEFAULT 'THU_VIEC', CONSTRAINT CK_NV_TrangThai CHECK (TrangThai IN ('THU_VIEC','CHINH_THUC','NGHI_VIEC')));
CREATE TABLE QuyetDinhTuyenDung (MaQDTD INT IDENTITY PRIMARY KEY, MaNV INT NOT NULL REFERENCES NhanVien(MaNV), MaPhong INT NOT NULL REFERENCES PhongBan(MaPhong), SoQuyetDinh VARCHAR(40) NOT NULL UNIQUE, NgayKy DATE NOT NULL, TuNgay DATE NOT NULL, DenNgay DATE NOT NULL, LuongThuViec DECIMAL(18,2) NOT NULL CHECK (LuongThuViec>=0), DieuKhoan NVARCHAR(MAX) NOT NULL, KetQua VARCHAR(20) NOT NULL DEFAULT 'CHO_DANH_GIA', CONSTRAINT CK_TD_Ngay CHECK(DenNgay>=TuNgay), CONSTRAINT CK_TD_KetQua CHECK(KetQua IN ('CHO_DANH_GIA','DAT','KHONG_DAT')));
CREATE TABLE HopDong (MaHD INT IDENTITY PRIMARY KEY, MaNV INT NOT NULL REFERENCES NhanVien(MaNV), SoHopDong VARCHAR(40) NOT NULL UNIQUE, LoaiHopDong VARCHAR(20) NOT NULL, NgayKy DATE NOT NULL, TuNgay DATE NOT NULL, DenNgay DATE NULL, NoiDung NVARCHAR(MAX) NULL, TrangThai VARCHAR(20) NOT NULL DEFAULT 'HIEU_LUC', CONSTRAINT UQ_HopDong_MaHD_MaNV UNIQUE(MaHD,MaNV), CONSTRAINT CK_HD_Loai CHECK (LoaiHopDong IN ('DAI_HAN','NGAN_HAN')), CONSTRAINT CK_HD_Ngay CHECK (DenNgay IS NULL OR DenNgay>=TuNgay), CONSTRAINT CK_HD_TrangThai CHECK(TrangThai IN ('HIEU_LUC','HET_HAN','HUY')));
CREATE TABLE HoSoBaoHiem (MaBH INT IDENTITY PRIMARY KEY, MaNV INT NOT NULL REFERENCES NhanVien(MaNV), MaHD INT NOT NULL REFERENCES HopDong(MaHD), SoBHXH VARCHAR(30) NULL, SoBHYT VARCHAR(30) NULL, TuNgay DATE NOT NULL, DenNgay DATE NULL, TyLeBHXH DECIMAL(9,6) NOT NULL DEFAULT 0 CHECK(TyLeBHXH BETWEEN 0 AND 1), TyLeBHYT DECIMAL(9,6) NOT NULL DEFAULT 0 CHECK(TyLeBHYT BETWEEN 0 AND 1), MucLuongDong DECIMAL(18,2) NOT NULL CHECK(MucLuongDong>=0), CONSTRAINT FK_BH_HopDongNhanVien FOREIGN KEY(MaHD,MaNV) REFERENCES HopDong(MaHD,MaNV), CONSTRAINT CK_BH_Ngay CHECK(DenNgay IS NULL OR DenNgay>=TuNgay));
CREATE TABLE QuyetDinhChucVu (MaQDCV INT IDENTITY PRIMARY KEY, MaNV INT NOT NULL REFERENCES NhanVien(MaNV), MaChucVu INT NOT NULL REFERENCES ChucVu(MaChucVu), SoQuyetDinh VARCHAR(40) NOT NULL UNIQUE, NgayKy DATE NOT NULL, HieuLucTu DATE NOT NULL, PhuCapChucVu DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK(PhuCapChucVu>=0), GhiChu NVARCHAR(250) NULL);
CREATE TABLE QuyetDinhLuong (MaQDL INT IDENTITY PRIMARY KEY, MaNV INT NOT NULL REFERENCES NhanVien(MaNV), SoQuyetDinh VARCHAR(40) NOT NULL UNIQUE, NgayKy DATE NOT NULL, HieuLucTu DATE NOT NULL, LuongCoBan DECIMAL(18,2) NOT NULL CHECK(LuongCoBan>0), PhuCapLuong DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK(PhuCapLuong>=0), PhuCapDocHai DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK(PhuCapDocHai>=0));
CREATE TABLE CaLamViec (MaCa INT IDENTITY PRIMARY KEY, TenCa NVARCHAR(100) NOT NULL UNIQUE, GioBatDau TIME NOT NULL, GioKetThuc TIME NOT NULL, SoGioChuan DECIMAL(6,2) NOT NULL CHECK(SoGioChuan>0));
CREATE TABLE KeHoachTuan (MaKH INT IDENTITY PRIMARY KEY, MaPhong INT NOT NULL REFERENCES PhongBan(MaPhong), TuanBatDau DATE NOT NULL, NguoiLap INT NOT NULL REFERENCES NhanVien(MaNV), NgayLap DATE NOT NULL DEFAULT CONVERT(DATE,GETDATE()), GhiChu NVARCHAR(250) NULL, CONSTRAINT UQ_KeHoachTuan UNIQUE(MaPhong,TuanBatDau));
CREATE TABLE PhanCongCa (MaPC INT IDENTITY PRIMARY KEY, MaKH INT NOT NULL REFERENCES KeHoachTuan(MaKH), MaNV INT NOT NULL REFERENCES NhanVien(MaNV), MaCa INT NOT NULL REFERENCES CaLamViec(MaCa), NgayLam DATE NOT NULL, DuKienPhuTroi BIT NOT NULL DEFAULT 0, CONSTRAINT UQ_PhanCongCa UNIQUE(MaNV,NgayLam,MaCa));
CREATE TABLE ChamCong (MaCC INT IDENTITY PRIMARY KEY, MaNV INT NOT NULL REFERENCES NhanVien(MaNV), MaCa INT NOT NULL REFERENCES CaLamViec(MaCa), NgayCong DATE NOT NULL, GioThucTe DECIMAL(6,2) NOT NULL CHECK(GioThucTe BETWEEN 0 AND 24), TrangThai VARCHAR(20) NOT NULL DEFAULT 'CO_MAT', CONSTRAINT CK_CC_TrangThai CHECK(TrangThai IN ('CO_MAT','VANG','NGHI_PHEP')), CONSTRAINT UQ_ChamCong UNIQUE(MaNV,NgayCong,MaCa));
CREATE TABLE PhieuPhuTroi (MaPhieu INT IDENTITY PRIMARY KEY, SoPhieu VARCHAR(40) NOT NULL UNIQUE, NgayPhuTroi DATE NOT NULL, SoGio DECIMAL(6,2) NOT NULL CHECK(SoGio>0 AND SoGio<=24), HinhThuc VARCHAR(20) NOT NULL, HeSo DECIMAL(6,2) NOT NULL DEFAULT 1.5 CHECK(HeSo=1.5), CONSTRAINT CK_PT_HinhThuc CHECK(HinhThuc IN ('TANG_CA','NGAY_LE','NGAY_NGHI')));
CREATE TABLE ChiTietPhuTroi (MaCT INT IDENTITY PRIMARY KEY, MaPhieu INT NOT NULL REFERENCES PhieuPhuTroi(MaPhieu), MaNV INT NOT NULL REFERENCES NhanVien(MaNV), SoGioThamGia DECIMAL(6,2) NOT NULL CHECK(SoGioThamGia>0 AND SoGioThamGia<=24), CONSTRAINT UQ_CTPhuTroi UNIQUE(MaPhieu,MaNV));
CREATE TABLE QuyetDinhThuongPhat (MaQD INT IDENTITY PRIMARY KEY, SoQuyetDinh VARCHAR(40) NOT NULL UNIQUE, Loai VARCHAR(15) NOT NULL, DoiTuong VARCHAR(15) NOT NULL, MaNV INT NULL REFERENCES NhanVien(MaNV), MaPhong INT NULL REFERENCES PhongBan(MaPhong), NgayKy DATE NOT NULL, LyDo NVARCHAR(500) NOT NULL, SoTien DECIMAL(18,2) NOT NULL CHECK(SoTien>=0), HinhThuc VARCHAR(20) NOT NULL DEFAULT 'QUA_LUONG', NguoiKy NVARCHAR(120) NOT NULL, CONSTRAINT CK_QD_Loai CHECK(Loai IN ('THUONG','KY_LUAT')), CONSTRAINT CK_QD_DoiTuong CHECK((DoiTuong='CA_NHAN' AND MaNV IS NOT NULL AND MaPhong IS NULL) OR (DoiTuong='TAP_THE' AND MaPhong IS NOT NULL AND MaNV IS NULL)), CONSTRAINT CK_QD_HinhThuc CHECK(HinhThuc IN ('QUA_LUONG','TIEN_MAT')));
CREATE TABLE BangLuong (MaBangLuong INT IDENTITY PRIMARY KEY, Thang INT NOT NULL CHECK(Thang BETWEEN 1 AND 12), Nam INT NOT NULL CHECK(Nam BETWEEN 2000 AND 2100), NgayLap DATETIME2 NOT NULL DEFAULT SYSDATETIME(), TrangThai VARCHAR(15) NOT NULL DEFAULT 'NHAP', CONSTRAINT UQ_BangLuong UNIQUE(Thang,Nam), CONSTRAINT CK_BL_TrangThai CHECK(TrangThai IN ('NHAP','CHOT')));
CREATE TABLE ChiTietLuong (MaCTL INT IDENTITY PRIMARY KEY, MaBangLuong INT NOT NULL REFERENCES BangLuong(MaBangLuong), MaNV INT NOT NULL REFERENCES NhanVien(MaNV), LuongCoBan DECIMAL(18,2) NOT NULL, PhuCapLuong DECIMAL(18,2) NOT NULL, PhuCapDocHai DECIMAL(18,2) NOT NULL, PhuCapChucVu DECIMAL(18,2) NOT NULL, GioChuan DECIMAL(9,2) NOT NULL, GioLam DECIMAL(9,2) NOT NULL, GioPhuTroi DECIMAL(9,2) NOT NULL, TienPhuTroi DECIMAL(18,2) NOT NULL, TienThuong DECIMAL(18,2) NOT NULL, TienPhat DECIMAL(18,2) NOT NULL, TruBaoHiem DECIMAL(18,2) NOT NULL, ThucLinh DECIMAL(18,2) NOT NULL, CONSTRAINT UQ_ChiTietLuong UNIQUE(MaBangLuong,MaNV));
CREATE TABLE PhieuThuChi (MaPhieuTC INT IDENTITY PRIMARY KEY, MaQD INT NOT NULL REFERENCES QuyetDinhThuongPhat(MaQD), MaNV INT NULL REFERENCES NhanVien(MaNV), SoPhieu VARCHAR(40) NOT NULL UNIQUE, NgayPhieu DATE NOT NULL, Loai VARCHAR(10) NOT NULL, SoTien DECIMAL(18,2) NOT NULL CHECK(SoTien>=0), DaThucHien BIT NOT NULL DEFAULT 0, MaBangLuongApDung INT NULL REFERENCES BangLuong(MaBangLuong), CONSTRAINT CK_TC_Loai CHECK(Loai IN ('THU','CHI')), CONSTRAINT UQ_ThuChi_QD_NV UNIQUE(MaQD,MaNV));
GO
CREATE INDEX IX_QDL_NV_HieuLuc ON QuyetDinhLuong(MaNV,HieuLucTu DESC);
CREATE INDEX IX_QDCV_NV_HieuLuc ON QuyetDinhChucVu(MaNV,HieuLucTu DESC);
CREATE INDEX IX_CC_NV_Ngay ON ChamCong(MaNV,NgayCong);
CREATE INDEX IX_PT_Ngay ON PhieuPhuTroi(NgayPhuTroi);
CREATE INDEX IX_QD_NV_Ngay ON QuyetDinhThuongPhat(MaNV,NgayKy);
GO
INSERT INTO PhongBan(TenPhong,LoaiDonVi) VALUES (N'Tài chính kế toán',N'PHONG'),(N'Kỹ thuật',N'PHONG'),(N'Sản xuất',N'PHAN_XUONG');
INSERT INTO ChucVu(TenChucVu) VALUES (N'Nhân viên'),(N'Trưởng phòng'),(N'Quản đốc');
INSERT INTO CaLamViec(TenCa,GioBatDau,GioKetThuc,SoGioChuan) VALUES(N'Ca hành chính','08:00','17:00',8),(N'Ca sáng','06:00','14:00',8),(N'Ca chiều','14:00','22:00',8);
GO

SET NOCOUNT ON;
SET XACT_ABORT ON;
BEGIN TRY
 BEGIN TRAN;
 IF EXISTS (SELECT 1 FROM NhanVien) THROW 51000, N'Dữ liệu nhân viên đã tồn tại; không nạp seed lần hai.', 1;

 INSERT INTO PhongBan(TenPhong,LoaiDonVi) VALUES
 (N'Nhân sự hành chính',N'PHONG'),(N'Kinh doanh nội địa',N'PHONG'),
 (N'Kinh doanh xuất khẩu',N'PHONG'),(N'Kiểm soát chất lượng',N'PHONG'),
 (N'Nghiên cứu và phát triển',N'PHONG'),(N'Mua hàng và cung ứng',N'PHONG'),
 (N'Kho vận',N'PHONG'),(N'Bảo trì thiết bị',N'PHONG'),
 (N'Phân xưởng pha màu',N'PHAN_XUONG');
 INSERT INTO ChucVu(TenChucVu) VALUES
 (N'Phó phòng'),(N'Tổ trưởng'),(N'Kỹ thuật viên'),
 (N'Chuyên viên'),(N'Kế toán viên'),(N'Công nhân sản xuất');
 INSERT INTO CaLamViec(TenCa,GioBatDau,GioKetThuc,SoGioChuan) VALUES
 (N'Ca đêm','22:00','06:00',8),(N'Ca linh hoạt','09:00','18:00',8);

 CREATE TABLE #SeedNV(Seq INT NOT NULL PRIMARY KEY, MaNV INT NOT NULL, MaPhong INT NOT NULL, Nhom VARCHAR(12) NOT NULL);
 DECLARE @i INT=1,@maNV INT,@ph INT,@nhom VARCHAR(12),@hoten NVARCHAR(120);
 WHILE @i<=120
 BEGIN
   SET @ph = 1 + ((@i-1)%12);
   SET @nhom=CASE WHEN @i<=96 THEN 'CHINH_THUC' WHEN @i<=112 THEN 'THU_VIEC' ELSE 'NGHI_VIEC' END;
   SET @hoten=CASE (@i%20)
    WHEN 0 THEN N'Nguyễn Minh Anh' WHEN 1 THEN N'Trần Văn Bình'
    WHEN 2 THEN N'Lê Thị Châu' WHEN 3 THEN N'Phạm Quốc Dũng'
    WHEN 4 THEN N'Hoàng Ngọc Hạnh' WHEN 5 THEN N'Võ Thành Hưng'
    WHEN 6 THEN N'Đặng Thu Lan' WHEN 7 THEN N'Bùi Hoài Nam'
    WHEN 8 THEN N'Đỗ Thảo Nguyên' WHEN 9 THEN N'Phan Minh Phúc'
    WHEN 10 THEN N'Ngô Hải Quân' WHEN 11 THEN N'Lý Bảo Trâm'
    WHEN 12 THEN N'Vũ Đức Khang' WHEN 13 THEN N'Mai Khánh Linh'
    WHEN 14 THEN N'Đinh Gia Bảo' WHEN 15 THEN N'Tạ Phương Thảo'
    WHEN 16 THEN N'Huỳnh Tuấn Kiệt' WHEN 17 THEN N'Trương Yến Nhi'
    WHEN 18 THEN N'Đoàn Đức Tài' ELSE N'Nguyễn Thanh Vy' END;
   INSERT INTO NhanVien(HoTen,NgaySinh,DienThoai,DiaChi,NgayVaoLam,MaPhong,TrangThai)
   VALUES (@hoten, -- HoTen chỉ chứa họ và tên; MaNV được lưu ở cột riêng.
           DATEADD(DAY,(@i*37)%365,DATEFROMPARTS(1988+(@i%13),1,1)),
           CONCAT('09',RIGHT(CONCAT('00000000',@i+23400000),8)),
           CONCAT(N'Địa chỉ giả lập số ',@i,N', TP. Hồ Chí Minh'),
           CASE WHEN @i BETWEEN 97 AND 112 THEN DATEFROMPARTS(2026,10,1)
                ELSE DATEADD(DAY,(@i*3)%20,DATEFROMPARTS(2025,1,1)) END,@ph,@nhom);
   SET @maNV=CONVERT(INT,SCOPE_IDENTITY());
   INSERT INTO #SeedNV(Seq,MaNV,MaPhong,Nhom) VALUES(@i,@maNV,@ph,@nhom);
   SET @i=@i+1;
 END;

 -- Quyết định tuyển dụng, thời hạn thử việc, kết quả đánh giá.
 INSERT INTO QuyetDinhTuyenDung(MaNV,MaPhong,SoQuyetDinh,NgayKy,TuNgay,DenNgay,LuongThuViec,DieuKhoan,KetQua)
 SELECT s.MaNV,s.MaPhong,CONCAT('TD-',FORMAT(s.Seq,'0000'),'-2026'),
        CASE WHEN s.Nhom='THU_VIEC' THEN '2026-09-25' ELSE '2025-01-01' END,
        CASE WHEN s.Nhom='THU_VIEC' THEN '2026-10-01' ELSE '2025-01-06' END,
        CASE WHEN s.Nhom='THU_VIEC' THEN '2026-10-31' ELSE '2025-02-05' END,
        CONVERT(DECIMAL(18,2),5000000+(s.Seq%9)*250000),
        N'Hai bên tuân thủ nội quy; ghi nhận công việc và đánh giá theo kỳ thử việc (dữ liệu demo).',
        CASE WHEN s.Nhom='THU_VIEC' THEN 'CHO_DANH_GIA' WHEN s.Nhom='NGHI_VIEC' THEN 'KHONG_DAT' ELSE 'DAT' END
 FROM #SeedNV s;

 -- Hợp đồng, quyết định lương và chức vụ của nhân viên chính thức.
 INSERT INTO HopDong(MaNV,SoHopDong,LoaiHopDong,NgayKy,TuNgay,DenNgay,NoiDung,TrangThai)
 SELECT s.MaNV,CONCAT('HD-',FORMAT(s.Seq,'0000')),
        CASE WHEN s.Seq<=80 THEN 'DAI_HAN' ELSE 'NGAN_HAN' END,
        '2025-02-06','2025-02-10',CASE WHEN s.Seq<=80 THEN NULL ELSE '2027-02-09' END,
        N'Hợp đồng lao động mẫu, điều khoản minh họa cho bài thực hành.', 'HIEU_LUC'
 FROM #SeedNV s WHERE s.Nhom='CHINH_THUC';
 INSERT INTO QuyetDinhLuong(MaNV,SoQuyetDinh,NgayKy,HieuLucTu,LuongCoBan,PhuCapLuong,PhuCapDocHai)
 SELECT s.MaNV,CONCAT('L-',FORMAT(s.Seq,'0000'),'-A'), '2025-02-06','2025-02-10',
        CONVERT(DECIMAL(18,2),6800000+(s.Seq%13)*400000),
        CONVERT(DECIMAL(18,2),(s.Seq%4)*250000),
        CONVERT(DECIMAL(18,2),CASE WHEN s.MaPhong IN (3,12) THEN 650000 ELSE 0 END)
 FROM #SeedNV s WHERE s.Nhom='CHINH_THUC';
 -- Có lịch sử tăng lương ở tháng 10, giúp thử nghiệm chọn QĐ theo ngày hiệu lực.
 INSERT INTO QuyetDinhLuong(MaNV,SoQuyetDinh,NgayKy,HieuLucTu,LuongCoBan,PhuCapLuong,PhuCapDocHai)
 SELECT s.MaNV,CONCAT('L-',FORMAT(s.Seq,'0000'),'-B'), '2026-09-25','2026-10-01',
        CONVERT(DECIMAL(18,2),7200000+(s.Seq%13)*400000),
        CONVERT(DECIMAL(18,2),(s.Seq%4)*250000),
        CONVERT(DECIMAL(18,2),CASE WHEN s.MaPhong IN (3,12) THEN 650000 ELSE 0 END)
 FROM #SeedNV s WHERE s.Nhom='CHINH_THUC' AND s.Seq%5=0;
 INSERT INTO QuyetDinhChucVu(MaNV,MaChucVu,SoQuyetDinh,NgayKy,HieuLucTu,PhuCapChucVu,GhiChu)
 SELECT s.MaNV,CASE WHEN s.Seq%12=1 THEN 2 WHEN s.Seq%12=2 THEN 4 ELSE 1 END,
        CONCAT('CV-',FORMAT(s.Seq,'0000'),'-A'), '2025-02-06','2025-02-10',
        CONVERT(DECIMAL(18,2),CASE WHEN s.Seq%12=1 THEN 1800000 WHEN s.Seq%12=2 THEN 700000 ELSE 0 END),
        N'Quyết định chức vụ lần đầu (mẫu)'
 FROM #SeedNV s WHERE s.Nhom='CHINH_THUC';
 INSERT INTO QuyetDinhChucVu(MaNV,MaChucVu,SoQuyetDinh,NgayKy,HieuLucTu,PhuCapChucVu,GhiChu)
 SELECT s.MaNV,4,CONCAT('CV-',FORMAT(s.Seq,'0000'),'-B'), '2026-09-28','2026-10-01',900000,
        N'Bổ nhiệm tổ trưởng (mẫu)'
 FROM #SeedNV s WHERE s.Nhom='CHINH_THUC' AND s.Seq%16=0;

 -- Bảo hiểm mẫu chỉ cho hợp đồng DÀI HẠN, đúng MaHD + MaNV.
 INSERT INTO HoSoBaoHiem(MaNV,MaHD,SoBHXH,SoBHYT,TuNgay,TyLeBHXH,TyLeBHYT,MucLuongDong)
 SELECT s.MaNV,h.MaHD,CONCAT('BHXH-DEMO-',FORMAT(s.Seq,'0000')),
        CONCAT('BHYT-DEMO-',FORMAT(s.Seq,'0000')), '2025-02-10',
        CONVERT(DECIMAL(9,6),0.080000),CONVERT(DECIMAL(9,6),0.015000),
        CONVERT(DECIMAL(18,2),6800000+(s.Seq%13)*400000)
 FROM #SeedNV s JOIN HopDong h ON h.MaNV=s.MaNV
 WHERE h.LoaiHopDong='DAI_HAN';

 -- Kế hoạch 7 tuần: 07/09 tới 19/10/2026 (Thứ Hai).
 CREATE TABLE #Weeks(Idx INT NOT NULL PRIMARY KEY,TuanBatDau DATE NOT NULL);
 INSERT INTO #Weeks(Idx,TuanBatDau) VALUES
 (0,'2026-09-07'),(1,'2026-09-14'),(2,'2026-09-21'),(3,'2026-09-28'),
 (4,'2026-10-05'),(5,'2026-10-12'),(6,'2026-10-19');
 INSERT INTO KeHoachTuan(MaPhong,TuanBatDau,NguoiLap,NgayLap,GhiChu)
 SELECT p.MaPhong,w.TuanBatDau,manager.MaNV,
        DATEADD(DAY,-3,w.TuanBatDau),N'Phân ca, công chuẩn và phụ trội kế hoạch (demo)'
 FROM PhongBan p CROSS JOIN #Weeks w
 CROSS APPLY (SELECT TOP 1 s.MaNV FROM #SeedNV s WHERE s.MaPhong=p.MaPhong AND s.Nhom='CHINH_THUC' ORDER BY s.Seq) manager;
 CREATE TABLE #Days(d INT NOT NULL PRIMARY KEY);
 INSERT INTO #Days(d) VALUES(0),(1),(2),(3),(4);
 INSERT INTO PhanCongCa(MaKH,MaNV,MaCa,NgayLam,DuKienPhuTroi)
 SELECT k.MaKH,s.MaNV,
        CASE WHEN s.MaPhong IN (3,12) THEN CASE WHEN s.Seq%2=0 THEN 2 ELSE 3 END ELSE 1 END,
        DATEADD(DAY,d.d,w.TuanBatDau),
        CASE WHEN (s.Seq+d.d+w.Idx)%11=0 THEN 1 ELSE 0 END
 FROM #SeedNV s JOIN #Weeks w ON (s.Nhom='CHINH_THUC' OR (s.Nhom='THU_VIEC' AND w.Idx>=4))
 JOIN KeHoachTuan k ON k.MaPhong=s.MaPhong AND k.TuanBatDau=w.TuanBatDau
 CROSS JOIN #Days d;
 INSERT INTO ChamCong(MaNV,MaCa,NgayCong,GioThucTe,TrangThai)
 SELECT pc.MaNV,pc.MaCa,pc.NgayLam,
        CONVERT(DECIMAL(6,2),CASE WHEN (pc.MaNV+DAY(pc.NgayLam))%31=0 THEN 0
          WHEN (pc.MaNV+DAY(pc.NgayLam))%17=0 THEN 0
          WHEN (pc.MaNV+DAY(pc.NgayLam))%13=0 THEN 7 ELSE 8 END),
        CASE WHEN (pc.MaNV+DAY(pc.NgayLam))%31=0 THEN 'VANG'
             WHEN (pc.MaNV+DAY(pc.NgayLam))%17=0 THEN 'NGHI_PHEP' ELSE 'CO_MAT' END
 FROM PhanCongCa pc;

 -- Phiếu phụ trội (1,5x): mỗi phiếu có 2 nhân viên tham gia.
 CREATE TABLE #SeedPT(Seq INT NOT NULL PRIMARY KEY,MaPhieu INT NOT NULL,NgayPhuTroi DATE NOT NULL);
 SET @i=1;
 WHILE @i<=160
 BEGIN
   DECLARE @ptDate DATE=DATEADD(DAY,(@i-1)%5,
                       DATEADD(DAY,7*((@i-1)%7),CONVERT(DATE,'2026-09-07')));
   INSERT INTO PhieuPhuTroi(SoPhieu,NgayPhuTroi,SoGio,HinhThuc,HeSo)
   VALUES(CONCAT('PT-',FORMAT(@i,'0000')),@ptDate,
          CONVERT(DECIMAL(6,2),CASE WHEN @i%3=0 THEN 3 ELSE 2 END),
          CASE WHEN @i%9=0 THEN 'NGAY_LE' WHEN @i%7=0 THEN 'NGAY_NGHI' ELSE 'TANG_CA' END,1.5);
   INSERT INTO #SeedPT(Seq,MaPhieu,NgayPhuTroi) VALUES(@i,CONVERT(INT,SCOPE_IDENTITY()),@ptDate);
   SET @i=@i+1;
 END;
 INSERT INTO ChiTietPhuTroi(MaPhieu,MaNV,SoGioThamGia)
 SELECT pt.MaPhieu,s.MaNV,CONVERT(DECIMAL(6,2),CASE WHEN v.pos=1 THEN 2 ELSE 1 END)
 FROM #SeedPT pt CROSS JOIN (VALUES(1),(2))v(pos)
 JOIN #SeedNV s ON s.Seq=1+((pt.Seq*3+v.pos)%96);

 -- 120 QĐ thưởng/phạt: 80 cá nhân, 40 tập thể.
 CREATE TABLE #SeedQD(Seq INT NOT NULL PRIMARY KEY,MaQD INT NOT NULL,Loai VARCHAR(15),HinhThuc VARCHAR(20),SoTien DECIMAL(18,2),MaPhong INT NULL);
 SET @i=1;
 WHILE @i<=120
 BEGIN
   DECLARE @type VARCHAR(15)=CASE WHEN @i%5=0 THEN 'KY_LUAT' ELSE 'THUONG' END;
   DECLARE @mode VARCHAR(20)=CASE WHEN @i%6=0 THEN 'TIEN_MAT' ELSE 'QUA_LUONG' END;
   DECLARE @collective BIT=CASE WHEN @i%3=0 THEN 1 ELSE 0 END;
   DECLARE @amount DECIMAL(18,2)=CONVERT(DECIMAL(18,2),400000+(@i%14)*20000);
   DECLARE @dept INT=1+((@i-1)%12);
   DECLARE @target INT=(SELECT MaNV FROM #SeedNV WHERE Seq=1+((@i*7)%96));
   INSERT INTO QuyetDinhThuongPhat(SoQuyetDinh,Loai,DoiTuong,MaNV,MaPhong,NgayKy,LyDo,SoTien,HinhThuc,NguoiKy)
   VALUES(CONCAT('KTKL-',FORMAT(@i,'0000')),@type,CASE WHEN @collective=1 THEN 'TAP_THE' ELSE 'CA_NHAN' END,
          CASE WHEN @collective=1 THEN NULL ELSE @target END,
          CASE WHEN @collective=1 THEN @dept ELSE NULL END,
          CASE WHEN @i%2=0 THEN '2026-09-18' ELSE '2026-10-16' END,
          CASE WHEN @type='THUONG' THEN N'Khen thưởng thành tích làm việc (demo)'
               ELSE N'Kỷ luật theo biên bản nội bộ giả lập' END,
          @amount,@mode,N'Giám đốc (dữ liệu giả lập)');
   INSERT INTO #SeedQD(Seq,MaQD,Loai,HinhThuc,SoTien,MaPhong)
   VALUES(@i,CONVERT(INT,SCOPE_IDENTITY()),@type,@mode,@amount,CASE WHEN @collective=1 THEN @dept ELSE NULL END);
   SET @i=@i+1;
 END;
 -- Phiếu cá nhân, loại CHI cho thưởng và THU cho phạt.
 INSERT INTO PhieuThuChi(MaQD,MaNV,SoPhieu,NgayPhieu,Loai,SoTien,DaThucHien)
 SELECT q.MaQD,d.MaNV,CONCAT('TC-CN-',FORMAT(q.Seq,'0000')),
        CASE WHEN q.Seq%2=0 THEN '2026-09-19' ELSE '2026-10-17' END,
        CASE WHEN q.Loai='THUONG' THEN 'CHI' ELSE 'THU' END,q.SoTien,
        CASE WHEN q.HinhThuc='TIEN_MAT' THEN 1 ELSE 0 END
 FROM #SeedQD q JOIN QuyetDinhThuongPhat d ON d.MaQD=q.MaQD
 WHERE q.MaPhong IS NULL;
 -- Phân bổ tập thể cho 2 người CÙNG phòng: tổng mỗi nhóm đúng = mức QĐ.
 ;WITH Alloc AS(
 SELECT q.Seq,q.MaQD,q.Loai,q.HinhThuc,q.SoTien,s.MaNV,
        ROW_NUMBER() OVER(PARTITION BY q.MaQD ORDER BY s.Seq) AS rn
 FROM #SeedQD q JOIN #SeedNV s ON s.MaPhong=q.MaPhong AND s.Nhom='CHINH_THUC'
 WHERE q.MaPhong IS NOT NULL
 )
 INSERT INTO PhieuThuChi(MaQD,MaNV,SoPhieu,NgayPhieu,Loai,SoTien,DaThucHien)
 SELECT a.MaQD,a.MaNV,CONCAT('TC-TT-',FORMAT(a.Seq,'0000'),'-',a.rn),
        CASE WHEN a.Seq%2=0 THEN '2026-09-19' ELSE '2026-10-17' END,
        CASE WHEN a.Loai='THUONG' THEN 'CHI' ELSE 'THU' END,
        a.SoTien/2,CASE WHEN a.HinhThuc='TIEN_MAT' THEN 1 ELSE 0 END
 FROM Alloc a WHERE a.rn<=2;

 INSERT INTO BangLuong(Thang,Nam,NgayLap,TrangThai)
 VALUES(9,2026,'2026-10-01T09:00:00','CHOT');
 DECLARE @payroll INT=CONVERT(INT,SCOPE_IDENTITY());
 ;WITH SalaryInfo AS (
 SELECT s.MaNV,l.LuongCoBan,l.PhuCapLuong,l.PhuCapDocHai,
        COALESCE(cv.PhuCapChucVu,0) PhuCapChucVu,
        COALESCE(cc.GioLam,0) GioLam,COALESCE(pt.GioPT,0) GioPT,
        COALESCE(tp.Thuong,0) Thuong,COALESCE(tp.Phat,0) Phat,
        CASE WHEN h.LoaiHopDong='DAI_HAN' THEN
             COALESCE(bh.MucLuongDong*(bh.TyLeBHXH+bh.TyLeBHYT),0) ELSE 0 END BH
 FROM #SeedNV s
 JOIN HopDong h ON h.MaNV=s.MaNV
 OUTER APPLY (SELECT TOP 1 * FROM QuyetDinhLuong l
              WHERE l.MaNV=s.MaNV AND l.HieuLucTu<'2026-10-01'
              ORDER BY l.HieuLucTu DESC,l.MaQDL DESC) l
 OUTER APPLY (SELECT TOP 1 * FROM QuyetDinhChucVu c
              WHERE c.MaNV=s.MaNV AND c.HieuLucTu<'2026-10-01'
              ORDER BY c.HieuLucTu DESC,c.MaQDCV DESC) cv
 OUTER APPLY (SELECT TOP 1 * FROM HoSoBaoHiem b
              WHERE b.MaNV=s.MaNV AND b.MaHD=h.MaHD AND b.TuNgay<'2026-10-01'
              ORDER BY b.TuNgay DESC,b.MaBH DESC) bh
 OUTER APPLY (SELECT SUM(c.GioThucTe) GioLam FROM ChamCong c WHERE c.MaNV=s.MaNV
              AND c.NgayCong>='2026-09-01' AND c.NgayCong<'2026-10-01' AND c.TrangThai='CO_MAT') cc
 OUTER APPLY (SELECT SUM(x.SoGioThamGia*p.HeSo) GioPT
              FROM ChiTietPhuTroi x JOIN PhieuPhuTroi p ON p.MaPhieu=x.MaPhieu
              WHERE x.MaNV=s.MaNV AND p.NgayPhuTroi>='2026-09-01' AND p.NgayPhuTroi<'2026-10-01') pt
 OUTER APPLY (SELECT SUM(CASE WHEN q.Loai='THUONG' THEN x.SoTien ELSE 0 END) Thuong,
                     SUM(CASE WHEN q.Loai='KY_LUAT' THEN x.SoTien ELSE 0 END) Phat
              FROM PhieuThuChi x JOIN QuyetDinhThuongPhat q ON q.MaQD=x.MaQD
              WHERE x.MaNV=s.MaNV AND x.DaThucHien=0 AND x.MaBangLuongApDung IS NULL
              AND q.HinhThuc='QUA_LUONG' AND q.NgayKy>='2026-09-01' AND q.NgayKy<'2026-10-01') tp
 WHERE s.Nhom='CHINH_THUC'
 )
 INSERT INTO ChiTietLuong(MaBangLuong,MaNV,LuongCoBan,PhuCapLuong,PhuCapDocHai,
 PhuCapChucVu,GioChuan,GioLam,GioPhuTroi,TienPhuTroi,TienThuong,TienPhat,TruBaoHiem,ThucLinh)
 SELECT @payroll,MaNV,LuongCoBan,PhuCapLuong,PhuCapDocHai,PhuCapChucVu,
        176,GioLam,GioPT,
        CONVERT(DECIMAL(18,2),ROUND((LuongCoBan/176.0)*GioPT,2)),
        Thuong,Phat,BH,
        CONVERT(DECIMAL(18,2),ROUND((LuongCoBan/176.0)*GioLam,2)
          +ROUND((LuongCoBan/176.0)*GioPT,2)+PhuCapLuong+PhuCapDocHai+PhuCapChucVu+Thuong-Phat-BH,2)
 FROM SalaryInfo;
 UPDATE x SET MaBangLuongApDung=@payroll
 FROM PhieuThuChi x JOIN QuyetDinhThuongPhat q ON q.MaQD=x.MaQD
 JOIN ChiTietLuong c ON c.MaNV=x.MaNV AND c.MaBangLuong=@payroll
 WHERE x.DaThucHien=0 AND x.MaBangLuongApDung IS NULL
 AND q.HinhThuc='QUA_LUONG' AND q.NgayKy>='2026-09-01' AND q.NgayKy<'2026-10-01';

 IF EXISTS (SELECT 1 FROM HoSoBaoHiem b JOIN HopDong h ON h.MaHD=b.MaHD
            WHERE h.LoaiHopDong<>'DAI_HAN' OR b.MaNV<>h.MaNV)
   THROW 51001,N'Seed bảo hiểm không nhất quán hợp đồng.',1;
 IF EXISTS (SELECT 1 FROM (SELECT q.MaQD,q.SoTien,SUM(x.SoTien) Allocated
                 FROM QuyetDinhThuongPhat q JOIN PhieuThuChi x ON x.MaQD=q.MaQD
                 WHERE q.DoiTuong='TAP_THE' GROUP BY q.MaQD,q.SoTien
                ) a WHERE a.SoTien<>a.Allocated)
   THROW 51002,N'Seed phân bổ thưởng/phạt tập thể không cân bằng.',1;
 IF (SELECT COUNT(*) FROM ChiTietLuong WHERE MaBangLuong=@payroll)<>96
   THROW 51003,N'Số phiếu lương mẫu không đúng 96.',1;
 DROP TABLE #SeedQD;
 DROP TABLE #SeedPT;
 DROP TABLE #Days;
 DROP TABLE #Weeks;
 DROP TABLE #SeedNV;
 COMMIT TRAN;
 PRINT N'Hoàn tất dữ liệu mẫu lớn: phòng ban, 120 nhân viên, 7 tuần lịch, chấm công, thưởng/phạt và kỳ lương 09/2026.';
END TRY
BEGIN CATCH
 IF XACT_STATE()<>0 ROLLBACK TRAN;
 THROW;
END CATCH;
GO

SET XACT_ABORT ON;
BEGIN TRY
    BEGIN TRANSACTION;
    ;WITH TenMau(HoTenGoc) AS (
        SELECT v.HoTenGoc FROM (VALUES
          (N'Nguyễn Minh Anh'), (N'Trần Văn Bình'), (N'Lê Thị Châu'),
          (N'Phạm Quốc Dũng'), (N'Hoàng Ngọc Hạnh'), (N'Võ Thành Hưng'),
          (N'Đặng Thu Lan'), (N'Bùi Hoài Nam'), (N'Đỗ Thảo Nguyên'),
          (N'Phan Minh Phúc'), (N'Ngô Hải Quân'), (N'Lý Bảo Trâm'),
          (N'Vũ Đức Khang'), (N'Mai Khánh Linh'), (N'Đinh Gia Bảo'),
          (N'Tạ Phương Thảo'), (N'Huỳnh Tuấn Kiệt'), (N'Trương Yến Nhi'),
          (N'Đoàn Đức Tài'), (N'Nguyễn Thanh Vy')
        ) v(HoTenGoc)
    )
    UPDATE nv
      SET HoTen = tm.HoTenGoc
    FROM dbo.NhanVien AS nv
    INNER JOIN TenMau AS tm
      ON nv.HoTen = tm.HoTenGoc + N' ' + RIGHT('000' + CAST(nv.MaNV AS VARCHAR(10)), 3)
    WHERE nv.MaNV BETWEEN 1 AND 120;
    PRINT CONCAT(N'Số tên mẫu cũ được sửa: ', @@ROWCOUNT);
    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF XACT_STATE() <> 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
GO

SELECT N'PhongBan' Bang,COUNT(*) SoDong FROM PhongBan
UNION ALL SELECT N'ChucVu',COUNT(*) FROM ChucVu
UNION ALL SELECT N'NhanVien',COUNT(*) FROM NhanVien
UNION ALL SELECT N'QuyetDinhTuyenDung',COUNT(*) FROM QuyetDinhTuyenDung
UNION ALL SELECT N'HopDong',COUNT(*) FROM HopDong
UNION ALL SELECT N'HoSoBaoHiem',COUNT(*) FROM HoSoBaoHiem
UNION ALL SELECT N'QuyetDinhChucVu',COUNT(*) FROM QuyetDinhChucVu
UNION ALL SELECT N'QuyetDinhLuong',COUNT(*) FROM QuyetDinhLuong
UNION ALL SELECT N'CaLamViec',COUNT(*) FROM CaLamViec
UNION ALL SELECT N'KeHoachTuan',COUNT(*) FROM KeHoachTuan
UNION ALL SELECT N'PhanCongCa',COUNT(*) FROM PhanCongCa
UNION ALL SELECT N'ChamCong',COUNT(*) FROM ChamCong
UNION ALL SELECT N'PhieuPhuTroi',COUNT(*) FROM PhieuPhuTroi
UNION ALL SELECT N'ChiTietPhuTroi',COUNT(*) FROM ChiTietPhuTroi
UNION ALL SELECT N'QuyetDinhThuongPhat',COUNT(*) FROM QuyetDinhThuongPhat
UNION ALL SELECT N'PhieuThuChi',COUNT(*) FROM PhieuThuChi
UNION ALL SELECT N'BangLuong',COUNT(*) FROM BangLuong
UNION ALL SELECT N'ChiTietLuong',COUNT(*) FROM ChiTietLuong;


--USE QuanLyNhanSuDongA;
--SELECT DB_NAME() AS DatabaseName;
--SELECT COUNT(*) AS SoNhanVien FROM dbo.NhanVien;
--SELECT COUNT(*) AS SoPhongBan FROM dbo.PhongBan;
