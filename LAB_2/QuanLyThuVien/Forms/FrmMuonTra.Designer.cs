namespace QuanLyThuVien.Forms
{
    partial class FrmMuonTra
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.tabs = new System.Windows.Forms.TabControl();
            this.tabMuon = new System.Windows.Forms.TabPage();
            this.label7 = new System.Windows.Forms.Label();
            this.label6 = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.btnLapPhieu = new System.Windows.Forms.Button();
            this.btnBoSach = new System.Windows.Forms.Button();
            this.btnThemSach = new System.Windows.Forms.Button();
            this.dgvSachChon = new System.Windows.Forms.DataGridView();
            this.dgvSachCon = new System.Windows.Forms.DataGridView();
            this.dtHenTra = new System.Windows.Forms.DateTimePicker();
            this.dtNgayMuon = new System.Windows.Forms.DateTimePicker();
            this.cboNhanVienMuon = new System.Windows.Forms.ComboBox();
            this.btnKiemTra = new System.Windows.Forms.Button();
            this.lblTrangThai = new System.Windows.Forms.Label();
            this.cboDocGia = new System.Windows.Forms.ComboBox();
            this.tabTra = new System.Windows.Forms.TabPage();
            this.label11 = new System.Windows.Forms.Label();
            this.label10 = new System.Windows.Forms.Label();
            this.label9 = new System.Windows.Forms.Label();
            this.label8 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.numPhiPhat = new System.Windows.Forms.NumericUpDown();
            this.dtNgayTra = new System.Windows.Forms.DateTimePicker();
            this.dgvDangMuon = new System.Windows.Forms.DataGridView();
            this.btnDong = new System.Windows.Forms.Button();
            this.btnTraSach = new System.Windows.Forms.Button();
            this.btnTaiSachMuon = new System.Windows.Forms.Button();
            this.cboTinhTrang = new System.Windows.Forms.ComboBox();
            this.cboNhanVienTra = new System.Windows.Forms.ComboBox();
            this.cboDocGiaTra = new System.Windows.Forms.ComboBox();
            this.tabs.SuspendLayout();
            this.tabMuon.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.dgvSachChon)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.dgvSachCon)).BeginInit();
            this.tabTra.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.numPhiPhat)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.dgvDangMuon)).BeginInit();
            this.SuspendLayout();
            // 
            // tabs
            // 
            this.tabs.Controls.Add(this.tabMuon);
            this.tabs.Controls.Add(this.tabTra);
            this.tabs.Location = new System.Drawing.Point(12, 12);
            this.tabs.Name = "tabs";
            this.tabs.SelectedIndex = 0;
            this.tabs.Size = new System.Drawing.Size(1233, 694);
            this.tabs.TabIndex = 0;
            // 
            // tabMuon
            // 
            this.tabMuon.Controls.Add(this.label7);
            this.tabMuon.Controls.Add(this.label6);
            this.tabMuon.Controls.Add(this.label5);
            this.tabMuon.Controls.Add(this.label4);
            this.tabMuon.Controls.Add(this.label3);
            this.tabMuon.Controls.Add(this.label2);
            this.tabMuon.Controls.Add(this.btnLapPhieu);
            this.tabMuon.Controls.Add(this.btnBoSach);
            this.tabMuon.Controls.Add(this.btnThemSach);
            this.tabMuon.Controls.Add(this.dgvSachChon);
            this.tabMuon.Controls.Add(this.dgvSachCon);
            this.tabMuon.Controls.Add(this.dtHenTra);
            this.tabMuon.Controls.Add(this.dtNgayMuon);
            this.tabMuon.Controls.Add(this.cboNhanVienMuon);
            this.tabMuon.Controls.Add(this.btnKiemTra);
            this.tabMuon.Controls.Add(this.lblTrangThai);
            this.tabMuon.Controls.Add(this.cboDocGia);
            this.tabMuon.Location = new System.Drawing.Point(4, 30);
            this.tabMuon.Name = "tabMuon";
            this.tabMuon.Padding = new System.Windows.Forms.Padding(3);
            this.tabMuon.Size = new System.Drawing.Size(1225, 660);
            this.tabMuon.TabIndex = 0;
            this.tabMuon.Text = "Mượn sách";
            this.tabMuon.UseVisualStyleBackColor = true;
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Location = new System.Drawing.Point(753, 156);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(171, 21);
            this.label7.TabIndex = 16;
            this.label7.Text = "Sách đã chọn (tối đa 3):";
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(23, 156);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(147, 21);
            this.label6.TabIndex = 15;
            this.label6.Text = "Sách còn trong kho:";
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(835, 81);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(73, 21);
            this.label5.TabIndex = 14;
            this.label5.Text = "Ngày trả:";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(455, 83);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(96, 21);
            this.label4.TabIndex = 13;
            this.label4.Text = "Ngày mượn:";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(23, 83);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(152, 21);
            this.label3.TabIndex = 12;
            this.label3.Text = "Nhân viên lập phiếu:";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(23, 29);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(65, 21);
            this.label2.TabIndex = 11;
            this.label2.Text = "Độc giả:";
            // 
            // btnLapPhieu
            // 
            this.btnLapPhieu.Location = new System.Drawing.Point(879, 432);
            this.btnLapPhieu.Name = "btnLapPhieu";
            this.btnLapPhieu.Size = new System.Drawing.Size(190, 47);
            this.btnLapPhieu.TabIndex = 10;
            this.btnLapPhieu.Text = "Lập phiếu mượn";
            this.btnLapPhieu.UseVisualStyleBackColor = true;
            this.btnLapPhieu.Click += new System.EventHandler(this.btnLapPhieu_Click);
            // 
            // btnBoSach
            // 
            this.btnBoSach.Location = new System.Drawing.Point(610, 301);
            this.btnBoSach.Name = "btnBoSach";
            this.btnBoSach.Size = new System.Drawing.Size(122, 48);
            this.btnBoSach.TabIndex = 9;
            this.btnBoSach.Text = "<< Bỏ";
            this.btnBoSach.UseVisualStyleBackColor = true;
            this.btnBoSach.Click += new System.EventHandler(this.btnBoSach_Click);
            // 
            // btnThemSach
            // 
            this.btnThemSach.Location = new System.Drawing.Point(610, 247);
            this.btnThemSach.Name = "btnThemSach";
            this.btnThemSach.Size = new System.Drawing.Size(122, 48);
            this.btnThemSach.TabIndex = 8;
            this.btnThemSach.Text = "Thêm >>";
            this.btnThemSach.UseVisualStyleBackColor = true;
            this.btnThemSach.Click += new System.EventHandler(this.btnThemSach_Click);
            // 
            // dgvSachChon
            // 
            this.dgvSachChon.AllowUserToAddRows = false;
            this.dgvSachChon.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dgvSachChon.Location = new System.Drawing.Point(757, 190);
            this.dgvSachChon.Name = "dgvSachChon";
            this.dgvSachChon.ReadOnly = true;
            this.dgvSachChon.RowHeadersWidth = 51;
            this.dgvSachChon.RowTemplate.Height = 24;
            this.dgvSachChon.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.dgvSachChon.Size = new System.Drawing.Size(413, 225);
            this.dgvSachChon.TabIndex = 7;
            // 
            // dgvSachCon
            // 
            this.dgvSachCon.AllowUserToAddRows = false;
            this.dgvSachCon.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dgvSachCon.Location = new System.Drawing.Point(27, 190);
            this.dgvSachCon.Name = "dgvSachCon";
            this.dgvSachCon.ReadOnly = true;
            this.dgvSachCon.RowHeadersWidth = 51;
            this.dgvSachCon.RowTemplate.Height = 24;
            this.dgvSachCon.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.dgvSachCon.Size = new System.Drawing.Size(551, 324);
            this.dgvSachCon.TabIndex = 6;
            // 
            // dtHenTra
            // 
            this.dtHenTra.Location = new System.Drawing.Point(914, 75);
            this.dtHenTra.Name = "dtHenTra";
            this.dtHenTra.Size = new System.Drawing.Size(256, 29);
            this.dtHenTra.TabIndex = 5;
            // 
            // dtNgayMuon
            // 
            this.dtNgayMuon.Location = new System.Drawing.Point(557, 77);
            this.dtNgayMuon.Name = "dtNgayMuon";
            this.dtNgayMuon.Size = new System.Drawing.Size(256, 29);
            this.dtNgayMuon.TabIndex = 4;
            // 
            // cboNhanVienMuon
            // 
            this.cboNhanVienMuon.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cboNhanVienMuon.FormattingEnabled = true;
            this.cboNhanVienMuon.Location = new System.Drawing.Point(187, 75);
            this.cboNhanVienMuon.Name = "cboNhanVienMuon";
            this.cboNhanVienMuon.Size = new System.Drawing.Size(191, 29);
            this.cboNhanVienMuon.TabIndex = 3;
            // 
            // btnKiemTra
            // 
            this.btnKiemTra.Location = new System.Drawing.Point(453, 22);
            this.btnKiemTra.Name = "btnKiemTra";
            this.btnKiemTra.Size = new System.Drawing.Size(154, 35);
            this.btnKiemTra.TabIndex = 2;
            this.btnKiemTra.Text = "Kiểm tra điều kiện";
            this.btnKiemTra.UseVisualStyleBackColor = true;
            this.btnKiemTra.Click += new System.EventHandler(this.btnKiemTra_Click);
            // 
            // lblTrangThai
            // 
            this.lblTrangThai.AutoSize = true;
            this.lblTrangThai.Location = new System.Drawing.Point(635, 29);
            this.lblTrangThai.Name = "lblTrangThai";
            this.lblTrangThai.Size = new System.Drawing.Size(178, 21);
            this.lblTrangThai.TabIndex = 1;
            this.lblTrangThai.Text = "Đủ điều kiện mượn sách";
            // 
            // cboDocGia
            // 
            this.cboDocGia.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cboDocGia.FormattingEnabled = true;
            this.cboDocGia.Location = new System.Drawing.Point(187, 26);
            this.cboDocGia.Name = "cboDocGia";
            this.cboDocGia.Size = new System.Drawing.Size(191, 29);
            this.cboDocGia.TabIndex = 0;
            // 
            // tabTra
            // 
            this.tabTra.Controls.Add(this.label11);
            this.tabTra.Controls.Add(this.label10);
            this.tabTra.Controls.Add(this.label9);
            this.tabTra.Controls.Add(this.label8);
            this.tabTra.Controls.Add(this.label1);
            this.tabTra.Controls.Add(this.numPhiPhat);
            this.tabTra.Controls.Add(this.dtNgayTra);
            this.tabTra.Controls.Add(this.dgvDangMuon);
            this.tabTra.Controls.Add(this.btnDong);
            this.tabTra.Controls.Add(this.btnTraSach);
            this.tabTra.Controls.Add(this.btnTaiSachMuon);
            this.tabTra.Controls.Add(this.cboTinhTrang);
            this.tabTra.Controls.Add(this.cboNhanVienTra);
            this.tabTra.Controls.Add(this.cboDocGiaTra);
            this.tabTra.Location = new System.Drawing.Point(4, 30);
            this.tabTra.Name = "tabTra";
            this.tabTra.Padding = new System.Windows.Forms.Padding(3);
            this.tabTra.Size = new System.Drawing.Size(1225, 660);
            this.tabTra.TabIndex = 1;
            this.tabTra.Text = "Trả sách";
            this.tabTra.UseVisualStyleBackColor = true;
            // 
            // label11
            // 
            this.label11.AutoSize = true;
            this.label11.Location = new System.Drawing.Point(543, 129);
            this.label11.Name = "label11";
            this.label11.Size = new System.Drawing.Size(70, 21);
            this.label11.TabIndex = 13;
            this.label11.Text = "Phí phạt:";
            // 
            // label10
            // 
            this.label10.AutoSize = true;
            this.label10.Location = new System.Drawing.Point(541, 76);
            this.label10.Name = "label10";
            this.label10.Size = new System.Drawing.Size(84, 21);
            this.label10.TabIndex = 12;
            this.label10.Text = "Tình trạng:";
            // 
            // label9
            // 
            this.label9.AutoSize = true;
            this.label9.Location = new System.Drawing.Point(540, 25);
            this.label9.Name = "label9";
            this.label9.Size = new System.Drawing.Size(73, 21);
            this.label9.TabIndex = 11;
            this.label9.Text = "Ngày trả:";
            // 
            // label8
            // 
            this.label8.AutoSize = true;
            this.label8.Location = new System.Drawing.Point(35, 81);
            this.label8.Name = "label8";
            this.label8.Size = new System.Drawing.Size(146, 21);
            this.label8.TabIndex = 10;
            this.label8.Text = "Nhân viên nhận trả:";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(35, 23);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(123, 21);
            this.label1.TabIndex = 9;
            this.label1.Text = "Độc giả trả sách:";
            // 
            // numPhiPhat
            // 
            this.numPhiPhat.Location = new System.Drawing.Point(637, 127);
            this.numPhiPhat.Maximum = new decimal(new int[] {
            1000000000,
            0,
            0,
            0});
            this.numPhiPhat.Name = "numPhiPhat";
            this.numPhiPhat.Size = new System.Drawing.Size(279, 29);
            this.numPhiPhat.TabIndex = 8;
            // 
            // dtNgayTra
            // 
            this.dtNgayTra.Location = new System.Drawing.Point(636, 22);
            this.dtNgayTra.Name = "dtNgayTra";
            this.dtNgayTra.Size = new System.Drawing.Size(280, 29);
            this.dtNgayTra.TabIndex = 7;
            // 
            // dgvDangMuon
            // 
            this.dgvDangMuon.AllowUserToAddRows = false;
            this.dgvDangMuon.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dgvDangMuon.Location = new System.Drawing.Point(39, 198);
            this.dgvDangMuon.Name = "dgvDangMuon";
            this.dgvDangMuon.ReadOnly = true;
            this.dgvDangMuon.RowHeadersWidth = 51;
            this.dgvDangMuon.RowTemplate.Height = 24;
            this.dgvDangMuon.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.dgvDangMuon.Size = new System.Drawing.Size(1148, 384);
            this.dgvDangMuon.TabIndex = 6;
            // 
            // btnDong
            // 
            this.btnDong.Location = new System.Drawing.Point(1035, 103);
            this.btnDong.Name = "btnDong";
            this.btnDong.Size = new System.Drawing.Size(152, 37);
            this.btnDong.TabIndex = 5;
            this.btnDong.Text = "Đóng";
            this.btnDong.UseVisualStyleBackColor = true;
            this.btnDong.Click += new System.EventHandler(this.btnDong_Click);
            // 
            // btnTraSach
            // 
            this.btnTraSach.Location = new System.Drawing.Point(1035, 51);
            this.btnTraSach.Name = "btnTraSach";
            this.btnTraSach.Size = new System.Drawing.Size(152, 46);
            this.btnTraSach.TabIndex = 4;
            this.btnTraSach.Text = "Xác nhận trả sách";
            this.btnTraSach.UseVisualStyleBackColor = true;
            this.btnTraSach.Click += new System.EventHandler(this.btnTraSach_Click);
            // 
            // btnTaiSachMuon
            // 
            this.btnTaiSachMuon.Location = new System.Drawing.Point(120, 127);
            this.btnTaiSachMuon.Name = "btnTaiSachMuon";
            this.btnTaiSachMuon.Size = new System.Drawing.Size(206, 40);
            this.btnTaiSachMuon.TabIndex = 3;
            this.btnTaiSachMuon.Text = "Tải sách đang mượn";
            this.btnTaiSachMuon.UseVisualStyleBackColor = true;
            this.btnTaiSachMuon.Click += new System.EventHandler(this.btnTaiSachMuon_Click);
            // 
            // cboTinhTrang
            // 
            this.cboTinhTrang.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cboTinhTrang.FormattingEnabled = true;
            this.cboTinhTrang.Location = new System.Drawing.Point(637, 73);
            this.cboTinhTrang.Name = "cboTinhTrang";
            this.cboTinhTrang.Size = new System.Drawing.Size(279, 29);
            this.cboTinhTrang.TabIndex = 2;
            // 
            // cboNhanVienTra
            // 
            this.cboNhanVienTra.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cboNhanVienTra.FormattingEnabled = true;
            this.cboNhanVienTra.Location = new System.Drawing.Point(196, 73);
            this.cboNhanVienTra.Name = "cboNhanVienTra";
            this.cboNhanVienTra.Size = new System.Drawing.Size(239, 29);
            this.cboNhanVienTra.TabIndex = 1;
            // 
            // cboDocGiaTra
            // 
            this.cboDocGiaTra.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cboDocGiaTra.FormattingEnabled = true;
            this.cboDocGiaTra.Location = new System.Drawing.Point(196, 22);
            this.cboDocGiaTra.Name = "cboDocGiaTra";
            this.cboDocGiaTra.Size = new System.Drawing.Size(239, 29);
            this.cboDocGiaTra.TabIndex = 0;
            this.cboDocGiaTra.SelectedIndexChanged += new System.EventHandler(this.cboDocGiaTra_SelectedIndexChanged);
            // 
            // FrmMuonTra
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(9F, 21F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1257, 718);
            this.Controls.Add(this.tabs);
            this.Font = new System.Drawing.Font("Segoe UI", 9.5F);
            this.Margin = new System.Windows.Forms.Padding(4);
            this.Name = "FrmMuonTra";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Mượn - Trả sách";
            this.Load += new System.EventHandler(this.FrmMuonTra_Load);
            this.tabs.ResumeLayout(false);
            this.tabMuon.ResumeLayout(false);
            this.tabMuon.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.dgvSachChon)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.dgvSachCon)).EndInit();
            this.tabTra.ResumeLayout(false);
            this.tabTra.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.numPhiPhat)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.dgvDangMuon)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TabControl tabs;
        private System.Windows.Forms.TabPage tabMuon;
        private System.Windows.Forms.Button btnKiemTra;
        private System.Windows.Forms.Label lblTrangThai;
        private System.Windows.Forms.ComboBox cboDocGia;
        private System.Windows.Forms.TabPage tabTra;
        private System.Windows.Forms.Button btnLapPhieu;
        private System.Windows.Forms.Button btnBoSach;
        private System.Windows.Forms.Button btnThemSach;
        private System.Windows.Forms.DataGridView dgvSachChon;
        private System.Windows.Forms.DataGridView dgvSachCon;
        private System.Windows.Forms.DateTimePicker dtHenTra;
        private System.Windows.Forms.DateTimePicker dtNgayMuon;
        private System.Windows.Forms.ComboBox cboNhanVienMuon;
        private System.Windows.Forms.Label label7;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.NumericUpDown numPhiPhat;
        private System.Windows.Forms.DateTimePicker dtNgayTra;
        private System.Windows.Forms.DataGridView dgvDangMuon;
        private System.Windows.Forms.Button btnDong;
        private System.Windows.Forms.Button btnTraSach;
        private System.Windows.Forms.Button btnTaiSachMuon;
        private System.Windows.Forms.ComboBox cboTinhTrang;
        private System.Windows.Forms.ComboBox cboNhanVienTra;
        private System.Windows.Forms.ComboBox cboDocGiaTra;
        private System.Windows.Forms.Label label11;
        private System.Windows.Forms.Label label10;
        private System.Windows.Forms.Label label9;
        private System.Windows.Forms.Label label8;
        private System.Windows.Forms.Label label1;
    }
}