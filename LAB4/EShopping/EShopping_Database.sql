IF DB_ID(N'EShopping') IS NULL
BEGIN
    CREATE DATABASE EShopping;
END
GO
USE EShopping;
GO
IF OBJECT_ID(N'dbo.KhachHang', N'U') IS NULL
BEGIN
    CREATE TABLE KhachHang
    (
        MaKH                INT IDENTITY(1,1) PRIMARY KEY,
        HoTen               NVARCHAR(150) NOT NULL,
        NgaySinh            DATE NULL,
        SoGiayTo            VARCHAR(30) NULL,
        LoaiGiayTo          VARCHAR(20) NULL,
        DiaChi              NVARCHAR(300) NULL,
        DienThoai           VARCHAR(20) NULL,
        Email               VARCHAR(255) NULL,
        NgayDangKy          DATETIME2 NOT NULL DEFAULT SYSDATETIME()
    );
END
GO
IF OBJECT_ID(N'dbo.TaiKhoan', N'U') IS NULL
BEGIN
    CREATE TABLE TaiKhoan
    (
        MaTaiKhoan          INT IDENTITY(1,1) PRIMARY KEY,
        MaKH                INT NOT NULL UNIQUE,
        TenDangNhap         VARCHAR(50) NOT NULL UNIQUE,
        MatKhau             NVARCHAR(255) NOT NULL,
        TrangThai           BIT NOT NULL DEFAULT 1,
        CONSTRAINT FK_TaiKhoan_KhachHang
            FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH)
    );
END
GO
IF OBJECT_ID(N'dbo.LoaiPhieuDatHang', N'U') IS NULL
BEGIN
    CREATE TABLE LoaiPhieuDatHang
    (
        MaLoaiPhieu         INT IDENTITY(1,1) PRIMARY KEY,
        TenLoaiPhieu        NVARCHAR(100) NOT NULL UNIQUE,
        ThoiGianXuLyGio     INT NOT NULL,
        MienPhiTu           DECIMAL(18,2) NULL,
        MoTa                NVARCHAR(300) NULL,
        TrangThai           BIT NOT NULL DEFAULT 1,
        CONSTRAINT CK_LoaiPhieu_ThoiGian
            CHECK (ThoiGianXuLyGio >= 0),
        CONSTRAINT CK_LoaiPhieu_MienPhi
            CHECK (MienPhiTu IS NULL OR MienPhiTu >= 0)
    );
END
GO
IF OBJECT_ID(N'dbo.KhuVucGiaoHang', N'U') IS NULL
BEGIN
    CREATE TABLE KhuVucGiaoHang
    (
        MaKhuVuc            INT IDENTITY(1,1) PRIMARY KEY,
        TenKhuVuc           NVARCHAR(150) NOT NULL UNIQUE,
        MoTa                NVARCHAR(300) NULL,
        TrangThai           BIT NOT NULL DEFAULT 1
    );
END
GO
IF OBJECT_ID(N'dbo.BangPhiGiaoHang', N'U') IS NULL
BEGIN
    CREATE TABLE BangPhiGiaoHang
    (
        MaKhuVuc            INT NOT NULL,
        MaLoaiPhieu         INT NOT NULL,
        PhiGiaoHang         DECIMAL(18,2) NOT NULL,
        PRIMARY KEY (MaKhuVuc, MaLoaiPhieu),
        CONSTRAINT FK_BangPhi_KhuVuc
            FOREIGN KEY (MaKhuVuc) REFERENCES KhuVucGiaoHang(MaKhuVuc),
        CONSTRAINT FK_BangPhi_LoaiPhieu
            FOREIGN KEY (MaLoaiPhieu) REFERENCES LoaiPhieuDatHang(MaLoaiPhieu),
        CONSTRAINT CK_BangPhi_Phi
            CHECK (PhiGiaoHang >= 0)
    );
END
GO
IF OBJECT_ID(N'dbo.DonHang', N'U') IS NULL
BEGIN
    CREATE TABLE DonHang
    (
        MaDonHang               BIGINT IDENTITY(1,1) PRIMARY KEY,
        SoDonHang               VARCHAR(30) NOT NULL UNIQUE,
        MaKH                    INT NOT NULL,
        MaLoaiPhieu             INT NOT NULL,
        MaKhuVuc                INT NOT NULL,
        HoTenNguoiNhan          NVARCHAR(150) NOT NULL,
        DiaChiNguoiNhan         NVARCHAR(300) NOT NULL,
        DienThoaiNguoiNhan      VARCHAR(20) NOT NULL,
        TongTienHang            DECIMAL(18,2) NOT NULL DEFAULT 0,
        PhiGiaoHang             DECIMAL(18,2) NOT NULL DEFAULT 0,
        TongThanhToan           AS (TongTienHang + PhiGiaoHang) PERSISTED,
        ThoiDiemDat             DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
        TrangThaiDonHang        NVARCHAR(50) NOT NULL DEFAULT N'Chờ xử lý',
        CONSTRAINT FK_DonHang_KhachHang
            FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH),
        CONSTRAINT FK_DonHang_LoaiPhieu
            FOREIGN KEY (MaLoaiPhieu) REFERENCES LoaiPhieuDatHang(MaLoaiPhieu),
        CONSTRAINT FK_DonHang_KhuVuc
            FOREIGN KEY (MaKhuVuc) REFERENCES KhuVucGiaoHang(MaKhuVuc),
        CONSTRAINT CK_DonHang_Tien
            CHECK (TongTienHang >= 0 AND PhiGiaoHang >= 0)
    );
END
GO
IF OBJECT_ID(N'dbo.ChiTietDonHang', N'U') IS NULL
BEGIN
    CREATE TABLE ChiTietDonHang
    (
        MaChiTiet            BIGINT IDENTITY(1,1) PRIMARY KEY,
        MaDonHang            BIGINT NOT NULL,
        MaSP                 VARCHAR(50) NOT NULL,
        TenSPTaiThoiDiemDat  NVARCHAR(250) NULL,
        SoLuong              INT NOT NULL,
        DonGia               DECIMAL(18,2) NOT NULL,
        ThanhTien            AS (SoLuong * DonGia) PERSISTED,
        CONSTRAINT FK_ChiTiet_DonHang
            FOREIGN KEY (MaDonHang) REFERENCES DonHang(MaDonHang),
        CONSTRAINT CK_ChiTiet_SoLuong
            CHECK (SoLuong > 0),
        CONSTRAINT CK_ChiTiet_DonGia
            CHECK (DonGia >= 0),
        CONSTRAINT UQ_ChiTiet_DonHang_SanPham
            UNIQUE (MaDonHang, MaSP)
    );
END
GO
IF OBJECT_ID(N'dbo.GiaoDichThanhToan', N'U') IS NULL
BEGIN
    CREATE TABLE GiaoDichThanhToan
    (
        MaGiaoDich                 BIGINT IDENTITY(1,1) PRIMARY KEY,
        MaDonHang                  BIGINT NOT NULL UNIQUE,
        LoaiThe                    VARCHAR(30) NOT NULL,
        SoTheDaChe                 VARCHAR(25) NULL,
        NgayHetHan                 DATE NULL,
        TenChuThe                  NVARCHAR(150) NULL,
        SoTienThanhToan            DECIMAL(18,2) NOT NULL,
        MaGiaoDichCongThanhToan    VARCHAR(100) NULL,
        ThoiDiemThanhToan          DATETIME2 NULL,
        TrangThaiThanhToan         NVARCHAR(50) NOT NULL DEFAULT N'Chờ thanh toán',
        ThongBaoGateway            NVARCHAR(500) NULL,
        CONSTRAINT FK_GiaoDich_DonHang
            FOREIGN KEY (MaDonHang) REFERENCES DonHang(MaDonHang),
        CONSTRAINT CK_GiaoDich_SoTien
            CHECK (SoTienThanhToan >= 0)
    );
END
GO
IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
    WHERE name = N'IX_DonHang_MaKH'
      AND object_id = OBJECT_ID(N'dbo.DonHang')
)
BEGIN
    CREATE INDEX IX_DonHang_MaKH ON DonHang(MaKH);
END
GO
IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
    WHERE name = N'IX_DonHang_ThoiDiemDat'
      AND object_id = OBJECT_ID(N'dbo.DonHang')
)
BEGIN
    CREATE INDEX IX_DonHang_ThoiDiemDat ON DonHang(ThoiDiemDat);
END
GO
IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
    WHERE name = N'IX_ChiTietDonHang_MaDonHang'
      AND object_id = OBJECT_ID(N'dbo.ChiTietDonHang')
)
BEGIN
    CREATE INDEX IX_ChiTietDonHang_MaDonHang
        ON ChiTietDonHang(MaDonHang);
END
GO
IF NOT EXISTS (SELECT 1 FROM LoaiPhieuDatHang)
BEGIN
    INSERT INTO LoaiPhieuDatHang
        (TenLoaiPhieu, ThoiGianXuLyGio, MienPhiTu, MoTa)
    VALUES
        (N'Đặt hàng thường', 72, NULL,
            N'Phiếu đặt hàng thông thường'),
        (N'Chuyển phát nhanh', 24, 1000000,
            N'Miễn phí từ 1.000.000đ'),
        (N'Chuyển phát nhanh trong ngày', 8, 5000000,
            N'Miễn phí từ 5.000.000đ');
END
GO
IF NOT EXISTS (SELECT 1 FROM KhuVucGiaoHang)
BEGIN
    INSERT INTO KhuVucGiaoHang (TenKhuVuc, MoTa)
    VALUES
        (N'Khu vực 1', N'Khu vực gần cửa hàng'),
        (N'Khu vực 2', N'Khu vực trong thành phố'),
        (N'Khu vực 3', N'Khu vực ngoài thành phố');
END
GO
IF NOT EXISTS (SELECT 1 FROM BangPhiGiaoHang)
BEGIN
    INSERT INTO BangPhiGiaoHang (MaKhuVuc, MaLoaiPhieu, PhiGiaoHang)
    SELECT kv.MaKhuVuc, lp.MaLoaiPhieu,
           CASE
               WHEN lp.TenLoaiPhieu = N'Đặt hàng thường' AND kv.TenKhuVuc = N'Khu vực 1' THEN 20000
               WHEN lp.TenLoaiPhieu = N'Đặt hàng thường' AND kv.TenKhuVuc = N'Khu vực 2' THEN 30000
               WHEN lp.TenLoaiPhieu = N'Đặt hàng thường' AND kv.TenKhuVuc = N'Khu vực 3' THEN 50000
               WHEN lp.TenLoaiPhieu = N'Chuyển phát nhanh' AND kv.TenKhuVuc = N'Khu vực 1' THEN 40000
               WHEN lp.TenLoaiPhieu = N'Chuyển phát nhanh' AND kv.TenKhuVuc = N'Khu vực 2' THEN 60000
               WHEN lp.TenLoaiPhieu = N'Chuyển phát nhanh' AND kv.TenKhuVuc = N'Khu vực 3' THEN 90000
               WHEN lp.TenLoaiPhieu = N'Chuyển phát nhanh trong ngày' AND kv.TenKhuVuc = N'Khu vực 1' THEN 80000
               WHEN lp.TenLoaiPhieu = N'Chuyển phát nhanh trong ngày' AND kv.TenKhuVuc = N'Khu vực 2' THEN 120000
               WHEN lp.TenLoaiPhieu = N'Chuyển phát nhanh trong ngày' AND kv.TenKhuVuc = N'Khu vực 3' THEN 180000
           END
    FROM KhuVucGiaoHang kv
    CROSS JOIN LoaiPhieuDatHang lp;
END
GO