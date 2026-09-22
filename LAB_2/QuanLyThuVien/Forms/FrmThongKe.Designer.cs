namespace QuanLyThuVien.Forms
{
    partial class FrmThongKe
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
            this.dtTu = new System.Windows.Forms.DateTimePicker();
            this.dtDen = new System.Windows.Forms.DateTimePicker();
            this.lblMuon = new System.Windows.Forms.Label();
            this.lblQuaHan = new System.Windows.Forms.Label();
            this.lblMat = new System.Windows.Forms.Label();
            this.lblHuHong = new System.Windows.Forms.Label();
            this.lblPhiPhat = new System.Windows.Forms.Label();
            this.dgvPhat = new System.Windows.Forms.DataGridView();
            this.btnThongKe = new System.Windows.Forms.Button();
            this.label6 = new System.Windows.Forms.Label();
            this.label7 = new System.Windows.Forms.Label();
            this.backgroundWorker1 = new System.ComponentModel.BackgroundWorker();
            this.btnDong = new System.Windows.Forms.Button();
            this.lblChiTietPhat = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.dgvPhat)).BeginInit();
            this.SuspendLayout();
            // 
            // dtTu
            // 
            this.dtTu.Location = new System.Drawing.Point(120, 12);
            this.dtTu.Name = "dtTu";
            this.dtTu.Size = new System.Drawing.Size(273, 29);
            this.dtTu.TabIndex = 0;
            this.dtTu.ValueChanged += new System.EventHandler(this.dtTu_ValueChanged);
            // 
            // dtDen
            // 
            this.dtDen.Location = new System.Drawing.Point(527, 12);
            this.dtDen.Name = "dtDen";
            this.dtDen.Size = new System.Drawing.Size(273, 29);
            this.dtDen.TabIndex = 1;
            // 
            // lblMuon
            // 
            this.lblMuon.AutoSize = true;
            this.lblMuon.Location = new System.Drawing.Point(120, 72);
            this.lblMuon.Name = "lblMuon";
            this.lblMuon.Size = new System.Drawing.Size(130, 21);
            this.lblMuon.TabIndex = 2;
            this.lblMuon.Text = "Lượt sách mượn: ";
            // 
            // lblQuaHan
            // 
            this.lblQuaHan.AutoSize = true;
            this.lblQuaHan.Location = new System.Drawing.Point(377, 72);
            this.lblQuaHan.Name = "lblQuaHan";
            this.lblQuaHan.Size = new System.Drawing.Size(106, 21);
            this.lblQuaHan.TabIndex = 3;
            this.lblQuaHan.Text = "Sách quá hạn:";
            // 
            // lblMat
            // 
            this.lblMat.AutoSize = true;
            this.lblMat.Location = new System.Drawing.Point(120, 121);
            this.lblMat.Name = "lblMat";
            this.lblMat.Size = new System.Drawing.Size(77, 21);
            this.lblMat.TabIndex = 4;
            this.lblMat.Text = "Sách mất:";
            // 
            // lblHuHong
            // 
            this.lblHuHong.AutoSize = true;
            this.lblHuHong.Location = new System.Drawing.Point(377, 121);
            this.lblHuHong.Name = "lblHuHong";
            this.lblHuHong.Size = new System.Drawing.Size(108, 21);
            this.lblHuHong.TabIndex = 5;
            this.lblHuHong.Text = "Sách hư hỏng:";
            // 
            // lblPhiPhat
            // 
            this.lblPhiPhat.AutoSize = true;
            this.lblPhiPhat.Font = new System.Drawing.Font("Segoe UI", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblPhiPhat.Location = new System.Drawing.Point(119, 171);
            this.lblPhiPhat.Name = "lblPhiPhat";
            this.lblPhiPhat.Size = new System.Drawing.Size(150, 28);
            this.lblPhiPhat.TabIndex = 6;
            this.lblPhiPhat.Text = "Tổng phí phạt:";
            // 
            // dgvPhat
            // 
            this.dgvPhat.AllowUserToAddRows = false;
            this.dgvPhat.AllowUserToDeleteRows = false;
            this.dgvPhat.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dgvPhat.Location = new System.Drawing.Point(50, 250);
            this.dgvPhat.Name = "dgvPhat";
            this.dgvPhat.ReadOnly = true;
            this.dgvPhat.RowHeadersWidth = 51;
            this.dgvPhat.RowTemplate.Height = 24;
            this.dgvPhat.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.dgvPhat.Size = new System.Drawing.Size(952, 360);
            this.dgvPhat.TabIndex = 7;
            // 
            // btnThongKe
            // 
            this.btnThongKe.Location = new System.Drawing.Point(848, 72);
            this.btnThongKe.Name = "btnThongKe";
            this.btnThongKe.Size = new System.Drawing.Size(154, 46);
            this.btnThongKe.TabIndex = 8;
            this.btnThongKe.Text = "Thống kê";
            this.btnThongKe.UseVisualStyleBackColor = true;
            this.btnThongKe.Click += new System.EventHandler(this.btnThongKe_Click);
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(46, 12);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(68, 21);
            this.label6.TabIndex = 9;
            this.label6.Text = "Từ ngày:";
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Location = new System.Drawing.Point(442, 12);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(79, 21);
            this.label7.TabIndex = 10;
            this.label7.Text = "Đến ngày:";
            // 
            // btnDong
            // 
            this.btnDong.Location = new System.Drawing.Point(848, 132);
            this.btnDong.Name = "btnDong";
            this.btnDong.Size = new System.Drawing.Size(154, 46);
            this.btnDong.TabIndex = 11;
            this.btnDong.Text = "Đóng";
            this.btnDong.UseVisualStyleBackColor = true;
            this.btnDong.Click += new System.EventHandler(this.btnDong_Click);
            // 
            // lblChiTietPhat
            // 
            this.lblChiTietPhat.AutoSize = true;
            this.lblChiTietPhat.Font = new System.Drawing.Font("Segoe UI", 9.5F);
            this.lblChiTietPhat.Location = new System.Drawing.Point(50, 220);
            this.lblChiTietPhat.Name = "lblChiTietPhat";
            this.lblChiTietPhat.Size = new System.Drawing.Size(140, 21);
            this.lblChiTietPhat.TabIndex = 12;
            this.lblChiTietPhat.Text = "Chi tiết phiếu phạt:";
            // 
            // FrmThongKe
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(9F, 21F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1052, 653);
            this.Controls.Add(this.lblChiTietPhat);
            this.Controls.Add(this.btnDong);
            this.Controls.Add(this.label7);
            this.Controls.Add(this.label6);
            this.Controls.Add(this.btnThongKe);
            this.Controls.Add(this.dgvPhat);
            this.Controls.Add(this.lblPhiPhat);
            this.Controls.Add(this.lblHuHong);
            this.Controls.Add(this.lblMat);
            this.Controls.Add(this.lblQuaHan);
            this.Controls.Add(this.lblMuon);
            this.Controls.Add(this.dtDen);
            this.Controls.Add(this.dtTu);
            this.Font = new System.Drawing.Font("Segoe UI", 9.5F);
            this.Margin = new System.Windows.Forms.Padding(4);
            this.Name = "FrmThongKe";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Thống kê";
            this.Load += new System.EventHandler(this.FrmThongKe_Load);
            ((System.ComponentModel.ISupportInitialize)(this.dgvPhat)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.DateTimePicker dtTu;
        private System.Windows.Forms.DateTimePicker dtDen;
        private System.Windows.Forms.Label lblMuon;
        private System.Windows.Forms.Label lblQuaHan;
        private System.Windows.Forms.Label lblMat;
        private System.Windows.Forms.Label lblHuHong;
        private System.Windows.Forms.Label lblPhiPhat;
        private System.Windows.Forms.DataGridView dgvPhat;
        private System.Windows.Forms.Button btnThongKe;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.Label label7;
        private System.ComponentModel.BackgroundWorker backgroundWorker1;
        private System.Windows.Forms.Button btnDong;
        private System.Windows.Forms.Label lblChiTietPhat;
    }
}