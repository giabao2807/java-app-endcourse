import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.event.ActionEvent;

public class De01_102190252_DInhGiaBao extends JFrame {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtInputtxt;
	private JTextField txtThtBaCh;
	private static DonHangDao dao = new DonHangDao();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					De01_102190252_DInhGiaBao frame = new De01_102190252_DInhGiaBao();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public De01_102190252_DInhGiaBao() {

		setTitle("Quan ly don hang");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 488, 363);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel Lb1 = new JLabel("Import data");
		Lb1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		Lb1.setBounds(32, 10, 124, 56);
		contentPane.add(Lb1);

		JLabel lblKeyword = new JLabel("Keyword");
		lblKeyword.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblKeyword.setBounds(32, 60, 124, 56);
		contentPane.add(lblKeyword);

		txtInputtxt = new JTextField();
		txtInputtxt.setHorizontalAlignment(SwingConstants.RIGHT);
		txtInputtxt.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		txtInputtxt.setText("input.txt");
		txtInputtxt.setBounds(154, 19, 160, 31);
		contentPane.add(txtInputtxt);
		txtInputtxt.setColumns(10);

		txtThtBaCh = new JTextField();
		txtThtBaCh.setHorizontalAlignment(SwingConstants.RIGHT);
		txtThtBaCh.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		txtThtBaCh.setText("Th\u1ECBt ba ch\u1EC9");
		txtThtBaCh.setColumns(10);
		txtThtBaCh.setBounds(154, 69, 307, 31);
		contentPane.add(txtThtBaCh);

		JButton btnNewButton = new JButton("Import file");
		
		btnNewButton.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnNewButton.setBounds(324, 17, 137, 31);
		contentPane.add(btnNewButton);

		JButton btnSoLuong = new JButton("So luong");
		btnSoLuong.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnSoLuong.setBounds(32, 110, 137, 31);
		contentPane.add(btnSoLuong);

		JButton btnTongTien = new JButton("Tong tien");
		btnTongTien.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnTongTien.setBounds(179, 110, 137, 31);
		contentPane.add(btnTongTien);

		JButton btnGoiY = new JButton("Goi y");
		btnGoiY.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnGoiY.setBounds(324, 110, 137, 31);
		contentPane.add(btnGoiY);

		JTextArea textArea = new JTextArea();
		textArea.setBounds(32, 151, 429, 165);
		contentPane.add(textArea);
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = txtInputtxt.getText().trim();
				File data = new File(fileName);
				List<String> fileContent = FileUtils.readLines(data);
				List<DonHang> dhs = FileUtils.convertDH(fileContent);
				dao.save(dhs);
				for(DonHang dh : dhs) {
				textArea.append(dh.toString()+"\n");
				}
			}
		});
		
		btnSoLuong.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tenmh = txtThtBaCh.getText().trim();
				long sl = dao.getAllDH().stream().filter(d->d.getTenMatHang().equals(tenmh)).count();
				if(sl>0) {
					textArea.setText(String.valueOf(sl));
				} else {
					textArea.setText("Khong tim thay mat hang nay");
				}
			}
		});
		
		btnTongTien.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String tenkh = txtThtBaCh.getText().trim();
				double soTien =0;
				for(DonHang dh : dao.getAllDH()) {
					if(dh.getTenNguoiMua().equals(tenkh)) {
						soTien+= dh.getSoTien();
					}
				}
				if(soTien>0) {
					textArea.setText(String.valueOf(soTien));
				} else {
					textArea.setText("Khong tim thay khach hang nay");
				}
				
			}
		});
		
		btnGoiY.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String tmh = txtThtBaCh.getText().trim();
				List<Item> items = getListItemGoiY(tmh,dao.getAllDH());
				for(Item i: items) {
					textArea.append(i.toString());
				}
			}
		});
	}
	private static List<Item> getListItemGoiY(String tmh, List<DonHang> dhs){
		List<String> tenkh = new ArrayList<>();
		List<Item> items = new ArrayList<>();
		for(DonHang dh : dhs) {
			if(dh.getTenMatHang().equals(tmh)) {
				tenkh.add(dh.getTenNguoiMua());
			}
		}
		for(String kh : tenkh) {
			for(DonHang dh : dhs) {
				if(dh.getTenNguoiMua().equals(kh)) {
					Item tmps = new Item(dh.getTenMatHang(), dh.getSoTien());
					if(isExistItem(items, tmps)) {
						for(Item i : items) {
							if(i.equals(tmps)) {
								i.setSotien(i.getSotien()+tmps.getSotien());
							}
						}
					} else {
						items.add(tmps);
					}
				}
			}
		}
		for(Item i : items) {
			if(i.getTenmh().equals(tmh)) {
				items.remove(i);
			}
		}
		return items;
	}
	private static boolean isExistItem(List<Item> list, Item item) {
		for(Item i : list) {
			if(item.equals(i)) {
				return true;
			}
		}
		return false;
	}
}

class Item{
	private String tenmh;
	private Double sotien;
	
	public Item(String tenmh, Double sotien) {
		super();
		this.tenmh = tenmh;
		this.sotien = sotien;
	}
	public String getTenmh() {
		return tenmh;
	}
	public void setTenmh(String tenmh) {
		this.tenmh = tenmh;
	}
	public Double getSotien() {
		return sotien;
	}
	public void setSotien(Double sotien) {
		this.sotien = sotien;
	}
	@Override
	public String toString() {
		return   tenmh + ", " + String.valueOf(sotien) ;
	}
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if (!(obj instanceof Item)) {
			return false;
		}
		Item that = (Item)obj;
		return getTenmh().equals(that.tenmh);
	}
	
}

class ConnectionProvider {
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3306/db_DonHang_123";
	private static final String USER = "root";
	private static final String PASSWORD = "12341234";
	private static Connection connection;

	public static Connection getConnection() {
		if (connection == null) {
			try {
				Class.forName(DRIVER);
				connection = DriverManager.getConnection(URL, USER, PASSWORD);
				// stmt = conn.createStatement();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return connection;
	}
}

class DonHang {
	private int id;
	private String TenMatHang;
	private Double SoTien;
	private String TenNguoiMua;

	public DonHang() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTenMatHang() {
		return TenMatHang;
	}

	public void setTenMatHang(String tenMatHang) {
		TenMatHang = tenMatHang;
	}

	public Double getSoTien() {
		return SoTien;
	}

	public void setSoTien(Double soTien) {
		SoTien = soTien;
	}

	public String getTenNguoiMua() {
		return TenNguoiMua;
	}

	public void setTenNguoiMua(String tenNguoiMua) {
		TenNguoiMua = tenNguoiMua;
	}

	public DonHang(int id, String tenMatHang, Double soTien, String tenNguoiMua) {
		super();
		this.id = id;
		TenMatHang = tenMatHang;
		SoTien = soTien;
		TenNguoiMua = tenNguoiMua;
	}

	@Override
	public String toString() {
		return "DonHang [id=" + id + ", TenMatHang=" + TenMatHang + ", SoTien=" + SoTien + ", TenNguoiMua="
				+ TenNguoiMua + "]";
	}
}

class FileUtils {

	private FileUtils() {

	}
	public static List<String> readLines(File file){
		try {
			return Files.readAllLines(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
	
	public static List<DonHang> convertDH(List<String> lines) {
		List<DonHang> rs = new ArrayList<>();
		int id = 0;
		for (String line : lines) {
			String[] tmps = line.split(",");
			if (tmps.length == 3) {
				DonHang dh = new DonHang(++id, tmps[0], Double.valueOf(tmps[1]), tmps[2]);
				rs.add(dh);
			}
		}
		return rs;
	}
}
 class DonHangDao {
	private Connection connection;
	private PreparedStatement pst;
	private ResultSet rs;
	private Statement st;

	public DonHangDao() {
		connection = ConnectionProvider.getConnection();
	}
	private static int count(int[] rs)
	{
		int count =0;
		for(int i : rs)
		{
			count +=i;
		}
		return count;
	}
	
	public boolean save(List<DonHang> list) {
		String query = "Insert into DonHang(TenMatHang, SoTien,TenNguoiMua)"
				+ "values(?,?,?)";
		try {
			pst = connection.prepareStatement(query);
			for (DonHang mh : list) {
				pst.setString(1,mh.getTenMatHang());
				pst.setDouble(2, mh.getSoTien());
				pst.setString(3, mh.getTenNguoiMua());
				pst.addBatch();
			}
			int[] rs = pst.executeBatch();
			return count(rs)>0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public List<DonHang> getAllDH(){
		List<DonHang> dhs = new ArrayList<>();
		String query = "Select * from DonHang";
		try {
			st = connection.createStatement();
			rs = pst.executeQuery(query);
			while (rs.next()) {
				DonHang dh = new DonHang(rs.getInt("id"), rs.getString("TenMatHang"), rs.getDouble("SoTien"), rs.getString("TenNguoiMua"));
				dhs.add(dh);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				st.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dhs;
		
	}
}
