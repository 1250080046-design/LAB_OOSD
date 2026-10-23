/*
    BAI 5 - QUAN LY HO SO BENH AN CUA BENH NHAN
    SQL Server. Script tao moi cac bang neu chua ton tai; khong xoa du lieu cu.
    Le phi nam mau 500000 VND chi la gia tri demo vi de bai khong quy dinh so tien.
*/
IF DB_ID(N'QuanLyHoSoBenhAn') IS NULL CREATE DATABASE QuanLyHoSoBenhAn;
GO
USE QuanLyHoSoBenhAn;
GO

IF OBJECT_ID(N'dbo.AnnualFeeRate', N'U') IS NULL
CREATE TABLE dbo.AnnualFeeRate(
    FeeYear int NOT NULL CONSTRAINT PK_AnnualFeeRate PRIMARY KEY,
    Amount decimal(18,2) NOT NULL CONSTRAINT CK_AnnualFeeRate_Amount CHECK(Amount > 0),
    Notes nvarchar(300) NULL,
    UpdatedAt datetime2 NOT NULL CONSTRAINT DF_AnnualFeeRate_UpdatedAt DEFAULT SYSDATETIME()
);
GO
IF OBJECT_ID(N'dbo.Patient', N'U') IS NULL
CREATE TABLE dbo.Patient(
    PatientID int IDENTITY(1,1) NOT NULL CONSTRAINT PK_Patient PRIMARY KEY,
    FullName nvarchar(150) NOT NULL,
    Address nvarchar(300) NOT NULL,
    DateOfBirth date NOT NULL,
    Phone varchar(25) NOT NULL,
    PhotoPath nvarchar(500) NULL,
    CreatedAt datetime2 NOT NULL CONSTRAINT DF_Patient_CreatedAt DEFAULT SYSDATETIME(),
    CONSTRAINT CK_Patient_FullName CHECK(LEN(LTRIM(RTRIM(FullName))) > 0),
    CONSTRAINT CK_Patient_Phone CHECK(LEN(LTRIM(RTRIM(Phone))) >= 7)
);
GO
IF OBJECT_ID(N'dbo.AnnualFeePayment', N'U') IS NULL
CREATE TABLE dbo.AnnualFeePayment(
    PaymentID bigint IDENTITY(1,1) NOT NULL CONSTRAINT PK_AnnualFeePayment PRIMARY KEY,
    PatientID int NOT NULL,
    FeeYear int NOT NULL,
    Amount decimal(18,2) NOT NULL CONSTRAINT CK_AnnualFeePayment_Amount CHECK(Amount > 0),
    PaidAt datetime2 NOT NULL CONSTRAINT DF_AnnualFeePayment_PaidAt DEFAULT SYSDATETIME(),
    PaymentMethod nvarchar(30) NOT NULL,
    Reference nvarchar(100) NULL,
    CONSTRAINT FK_AnnualFeePayment_Patient FOREIGN KEY(PatientID) REFERENCES dbo.Patient(PatientID),
    CONSTRAINT FK_AnnualFeePayment_Rate FOREIGN KEY(FeeYear) REFERENCES dbo.AnnualFeeRate(FeeYear),
    CONSTRAINT UQ_AnnualFeePayment_Patient_Year UNIQUE(PatientID, FeeYear)
);
GO
IF OBJECT_ID(N'dbo.MedicalRecord', N'U') IS NULL
CREATE TABLE dbo.MedicalRecord(
    RecordID int IDENTITY(1,1) NOT NULL CONSTRAINT PK_MedicalRecord PRIMARY KEY,
    RecordCode varchar(30) NOT NULL CONSTRAINT UQ_MedicalRecord_RecordCode UNIQUE,
    PatientID int NOT NULL CONSTRAINT UQ_MedicalRecord_Patient UNIQUE,
    IssuedAt datetime2 NOT NULL CONSTRAINT DF_MedicalRecord_IssuedAt DEFAULT SYSDATETIME(),
    Status nvarchar(20) NOT NULL CONSTRAINT DF_MedicalRecord_Status DEFAULT N'Đang hoạt động',
    CONSTRAINT FK_MedicalRecord_Patient FOREIGN KEY(PatientID) REFERENCES dbo.Patient(PatientID),
    CONSTRAINT CK_MedicalRecord_Status CHECK(Status IN (N'Đang hoạt động', N'Ngừng hoạt động'))
);
GO
IF OBJECT_ID(N'dbo.Department', N'U') IS NULL
CREATE TABLE dbo.Department(
    DepartmentID int IDENTITY(1,1) NOT NULL CONSTRAINT PK_Department PRIMARY KEY,
    DepartmentName nvarchar(120) NOT NULL CONSTRAINT UQ_Department_Name UNIQUE,
    IsActive bit NOT NULL CONSTRAINT DF_Department_IsActive DEFAULT 1
);
GO
IF OBJECT_ID(N'dbo.Doctor', N'U') IS NULL
CREATE TABLE dbo.Doctor(
    DoctorID int IDENTITY(1,1) NOT NULL CONSTRAINT PK_Doctor PRIMARY KEY,
    FullName nvarchar(150) NOT NULL,
    DepartmentID int NOT NULL,
    Phone varchar(25) NULL,
    IsActive bit NOT NULL CONSTRAINT DF_Doctor_IsActive DEFAULT 1,
    CONSTRAINT FK_Doctor_Department FOREIGN KEY(DepartmentID) REFERENCES dbo.Department(DepartmentID)
);
GO
IF OBJECT_ID(N'dbo.Appointment', N'U') IS NULL
CREATE TABLE dbo.Appointment(
    AppointmentID bigint IDENTITY(1,1) NOT NULL CONSTRAINT PK_Appointment PRIMARY KEY,
    PatientID int NOT NULL,
    DoctorID int NULL,
    AppointmentAt datetime2 NOT NULL,
    AppointmentYear int NOT NULL,
    QuarterNo tinyint NOT NULL,
    Status nvarchar(25) NOT NULL CONSTRAINT DF_Appointment_Status DEFAULT N'Đã hẹn',
    Notes nvarchar(500) NULL,
    CreatedAt datetime2 NOT NULL CONSTRAINT DF_Appointment_CreatedAt DEFAULT SYSDATETIME(),
    CONSTRAINT FK_Appointment_Patient FOREIGN KEY(PatientID) REFERENCES dbo.Patient(PatientID),
    CONSTRAINT FK_Appointment_Doctor FOREIGN KEY(DoctorID) REFERENCES dbo.Doctor(DoctorID),
    CONSTRAINT CK_Appointment_Quarter CHECK(QuarterNo BETWEEN 1 AND 4),
    CONSTRAINT CK_Appointment_Status CHECK(Status IN (N'Đã hẹn', N'Đã khám', N'Đã hủy')),
    CONSTRAINT UQ_Appointment_Patient_Quarter UNIQUE(PatientID, AppointmentYear, QuarterNo)
);
GO
IF OBJECT_ID(N'dbo.TreatmentEpisode', N'U') IS NULL
CREATE TABLE dbo.TreatmentEpisode(
    EpisodeID bigint IDENTITY(1,1) NOT NULL CONSTRAINT PK_TreatmentEpisode PRIMARY KEY,
    RecordID int NOT NULL,
    StartAt datetime2 NOT NULL CONSTRAINT DF_TreatmentEpisode_StartAt DEFAULT SYSDATETIME(),
    EndAt datetime2 NULL,
    Status nvarchar(25) NOT NULL CONSTRAINT DF_TreatmentEpisode_Status DEFAULT N'Đang điều trị',
    Notes nvarchar(1000) NULL,
    CONSTRAINT FK_TreatmentEpisode_Record FOREIGN KEY(RecordID) REFERENCES dbo.MedicalRecord(RecordID),
    CONSTRAINT CK_TreatmentEpisode_Status CHECK(Status IN (N'Đang điều trị', N'Đã hoàn tất')),
    CONSTRAINT CK_TreatmentEpisode_End CHECK((Status=N'Đang điều trị' AND EndAt IS NULL) OR (Status=N'Đã hoàn tất' AND EndAt IS NOT NULL))
);
GO
IF OBJECT_ID(N'dbo.Visit', N'U') IS NULL
CREATE TABLE dbo.Visit(
    VisitID bigint IDENTITY(1,1) NOT NULL CONSTRAINT PK_Visit PRIMARY KEY,
    EpisodeID bigint NOT NULL,
    AppointmentID bigint NULL,
    DoctorID int NOT NULL,
    DepartmentID int NOT NULL,
    VisitAt datetime2 NOT NULL CONSTRAINT DF_Visit_VisitAt DEFAULT SYSDATETIME(),
    VisitType nvarchar(20) NOT NULL CONSTRAINT DF_Visit_VisitType DEFAULT N'Khám điều trị',
    Symptoms nvarchar(1500) NOT NULL,
    Diagnosis nvarchar(1500) NULL,
    ClinicalNotes nvarchar(2000) NULL,
    CONSTRAINT FK_Visit_Episode FOREIGN KEY(EpisodeID) REFERENCES dbo.TreatmentEpisode(EpisodeID),
    CONSTRAINT FK_Visit_Appointment FOREIGN KEY(AppointmentID) REFERENCES dbo.Appointment(AppointmentID),
    CONSTRAINT FK_Visit_Doctor FOREIGN KEY(DoctorID) REFERENCES dbo.Doctor(DoctorID),
    CONSTRAINT FK_Visit_Department FOREIGN KEY(DepartmentID) REFERENCES dbo.Department(DepartmentID),
    CONSTRAINT CK_Visit_Type CHECK(VisitType IN (N'Khám điều trị', N'Khám định kỳ'))
);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name=N'UX_Visit_Appointment' AND object_id=OBJECT_ID(N'dbo.Visit'))
    CREATE UNIQUE INDEX UX_Visit_Appointment ON dbo.Visit(AppointmentID) WHERE AppointmentID IS NOT NULL;
GO
IF OBJECT_ID(N'dbo.LabTest', N'U') IS NULL
CREATE TABLE dbo.LabTest(
    TestID bigint IDENTITY(1,1) NOT NULL CONSTRAINT PK_LabTest PRIMARY KEY,
    VisitID bigint NOT NULL,
    TestName nvarchar(180) NOT NULL,
    RequestedAt datetime2 NOT NULL CONSTRAINT DF_LabTest_RequestedAt DEFAULT SYSDATETIME(),
    Result nvarchar(2000) NULL,
    ResultAt datetime2 NULL,
    Status nvarchar(25) NOT NULL CONSTRAINT DF_LabTest_Status DEFAULT N'Chờ kết quả',
    Notes nvarchar(500) NULL,
    CONSTRAINT FK_LabTest_Visit FOREIGN KEY(VisitID) REFERENCES dbo.Visit(VisitID),
    CONSTRAINT CK_LabTest_Status CHECK(Status IN (N'Chờ kết quả', N'Đã có kết quả', N'Đã hủy')),
    CONSTRAINT CK_LabTest_Result CHECK((Status=N'Đã có kết quả' AND Result IS NOT NULL AND ResultAt IS NOT NULL) OR Status<>N'Đã có kết quả')
);
GO
IF OBJECT_ID(N'dbo.Prescription', N'U') IS NULL
CREATE TABLE dbo.Prescription(
    PrescriptionID bigint IDENTITY(1,1) NOT NULL CONSTRAINT PK_Prescription PRIMARY KEY,
    VisitID bigint NOT NULL CONSTRAINT UQ_Prescription_Visit UNIQUE,
    DoctorID int NOT NULL,
    CreatedAt datetime2 NOT NULL CONSTRAINT DF_Prescription_CreatedAt DEFAULT SYSDATETIME(),
    Notes nvarchar(1500) NULL,
    Status nvarchar(20) NOT NULL CONSTRAINT DF_Prescription_Status DEFAULT N'Đã lập',
    CONSTRAINT FK_Prescription_Visit FOREIGN KEY(VisitID) REFERENCES dbo.Visit(VisitID),
    CONSTRAINT FK_Prescription_Doctor FOREIGN KEY(DoctorID) REFERENCES dbo.Doctor(DoctorID),
    CONSTRAINT CK_Prescription_Status CHECK(Status IN (N'Đã lập', N'Đã hủy'))
);
GO
IF OBJECT_ID(N'dbo.PrescriptionItem', N'U') IS NULL
CREATE TABLE dbo.PrescriptionItem(
    ItemID bigint IDENTITY(1,1) NOT NULL CONSTRAINT PK_PrescriptionItem PRIMARY KEY,
    PrescriptionID bigint NOT NULL,
    MedicineName nvarchar(180) NOT NULL,
    Dosage nvarchar(120) NOT NULL,
    Frequency nvarchar(250) NOT NULL,
    DurationDays int NOT NULL CONSTRAINT CK_PrescriptionItem_Duration CHECK(DurationDays>0),
    Quantity int NOT NULL CONSTRAINT CK_PrescriptionItem_Quantity CHECK(Quantity>0),
    Instructions nvarchar(500) NULL,
    CONSTRAINT FK_PrescriptionItem_Prescription FOREIGN KEY(PrescriptionID) REFERENCES dbo.Prescription(PrescriptionID)
);
GO
IF OBJECT_ID(N'dbo.HospitalInvoice', N'U') IS NULL
CREATE TABLE dbo.HospitalInvoice(
    InvoiceID bigint IDENTITY(1,1) NOT NULL CONSTRAINT PK_HospitalInvoice PRIMARY KEY,
    EpisodeID bigint NOT NULL CONSTRAINT UQ_HospitalInvoice_Episode UNIQUE,
    TotalAmount decimal(18,2) NOT NULL CONSTRAINT CK_HospitalInvoice_Total CHECK(TotalAmount>=0),
    CreatedAt datetime2 NOT NULL CONSTRAINT DF_HospitalInvoice_CreatedAt DEFAULT SYSDATETIME(),
    Status nvarchar(20) NOT NULL CONSTRAINT DF_HospitalInvoice_Status DEFAULT N'Chưa thanh toán',
    CONSTRAINT FK_HospitalInvoice_Episode FOREIGN KEY(EpisodeID) REFERENCES dbo.TreatmentEpisode(EpisodeID),
    CONSTRAINT CK_HospitalInvoice_Status CHECK(Status IN (N'Chưa thanh toán', N'Đã thanh toán'))
);
GO
IF OBJECT_ID(N'dbo.HospitalPayment', N'U') IS NULL
CREATE TABLE dbo.HospitalPayment(
    PaymentID bigint IDENTITY(1,1) NOT NULL CONSTRAINT PK_HospitalPayment PRIMARY KEY,
    InvoiceID bigint NOT NULL,
    Amount decimal(18,2) NOT NULL CONSTRAINT CK_HospitalPayment_Amount CHECK(Amount>0),
    PaidAt datetime2 NOT NULL CONSTRAINT DF_HospitalPayment_PaidAt DEFAULT SYSDATETIME(),
    PaymentMethod nvarchar(30) NOT NULL,
    Reference nvarchar(100) NULL,
    CONSTRAINT FK_HospitalPayment_Invoice FOREIGN KEY(InvoiceID) REFERENCES dbo.HospitalInvoice(InvoiceID)
);
GO

IF NOT EXISTS (SELECT 1 FROM dbo.AnnualFeeRate WHERE FeeYear=YEAR(GETDATE()))
    INSERT dbo.AnnualFeeRate(FeeYear, Amount, Notes) VALUES (YEAR(GETDATE()), 500000, N'Giá trị demo có thể điều chỉnh; đề không quy định mức lệ phí.');
IF NOT EXISTS (SELECT 1 FROM dbo.Department)
    INSERT dbo.Department(DepartmentName) VALUES (N'Nội tổng quát'),(N'Nhi khoa'),(N'Xét nghiệm'),(N'Ngoại khoa');
IF NOT EXISTS (SELECT 1 FROM dbo.Doctor)
    INSERT dbo.Doctor(FullName, DepartmentID, Phone)
    SELECT N'BS. Nguyễn Minh An', DepartmentID, '0901000001' FROM dbo.Department WHERE DepartmentName=N'Nội tổng quát'
    UNION ALL SELECT N'BS. Trần Thu Hà', DepartmentID, '0901000002' FROM dbo.Department WHERE DepartmentName=N'Nhi khoa'
    UNION ALL SELECT N'BS. Lê Quốc Bảo', DepartmentID, '0901000003' FROM dbo.Department WHERE DepartmentName=N'Ngoại khoa';
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name=N'IX_Patient_NamePhone' AND object_id=OBJECT_ID(N'dbo.Patient')) CREATE INDEX IX_Patient_NamePhone ON dbo.Patient(FullName, Phone);
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name=N'IX_Visit_EpisodeAt' AND object_id=OBJECT_ID(N'dbo.Visit')) CREATE INDEX IX_Visit_EpisodeAt ON dbo.Visit(EpisodeID, VisitAt DESC);
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name=N'IX_LabTest_Visit' AND object_id=OBJECT_ID(N'dbo.LabTest')) CREATE INDEX IX_LabTest_Visit ON dbo.LabTest(VisitID, RequestedAt DESC);
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name=N'IX_Appointment_Time' AND object_id=OBJECT_ID(N'dbo.Appointment')) CREATE INDEX IX_Appointment_Time ON dbo.Appointment(AppointmentAt, Status);
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name=N'IX_Episode_Status' AND object_id=OBJECT_ID(N'dbo.TreatmentEpisode')) CREATE INDEX IX_Episode_Status ON dbo.TreatmentEpisode(Status, StartAt DESC);
GO
PRINT N'Database QuanLyHoSoBenhAn đã sẵn sàng.';
