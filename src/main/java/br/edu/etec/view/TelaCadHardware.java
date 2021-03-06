package br.edu.etec.view;

import java.awt.Dimension;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.edu.etec.model.Cliente;
import br.edu.etec.model.Hardware;
import br.edu.etec.model.Id;
import br.edu.etec.persistence.ClienteJdbcDAO;
import br.edu.etec.persistence.HardwareJdbcDAO;

public class TelaCadHardware extends TelaDeCadastro {
	JList list = new JList();
	Hardware hardware = new Hardware();

	JLabel lbDescricao = new JLabel("Descrição");
	JTextField txtDescricao = new JTextField();

	JLabel lbQtdAtual = new JLabel("QtdAtual");
	JTextField txtQtdAtual = new JTextField();

	JLabel lbQtdMinima = new JLabel("QtdMinima");
	JTextField txtQtdMinima = new JTextField();

	JLabel lbPrecoUnit = new JLabel("Preco Unitario");
	JTextField txtPrecoUnit = new JTextField();

	static String[] colunas = { "id", "Descricao", "qtdAtual", "qtdMinima", "precoUnit" };

	public TelaCadHardware() {
		super(4, 2, colunas);
		this.painelParaCampos.add(lbDescricao);
		this.painelParaCampos.add(txtDescricao);

		this.painelParaCampos.add(lbPrecoUnit);
		this.painelParaCampos.add(txtPrecoUnit);

		this.painelParaCampos.add(lbQtdAtual);
		this.painelParaCampos.add(txtQtdAtual);

		this.painelParaCampos.add(lbQtdMinima);
		this.painelParaCampos.add(txtQtdMinima);
		try {
			TelaCadHardware.this.listar();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Construtor TelaCadHardware()");

		this.btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TelaCadHardware.this.limparFormulario();
			}
		});
		this.btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					TelaCadHardware.this.salvar();
					TelaCadHardware.this.listar();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		this.btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TelaCadHardware.this.cancelar();
			}
		});

		this.btnAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					TelaCadHardware.this.alterar();
					TelaCadHardware.this.listar();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		this.btnListar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					TelaCadHardware.this.listar();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		this.btnExcluir.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					TelaCadHardware.this.excluir();
					TelaCadHardware.this.listar();
				} catch (SQLException e1) {
					System.out.println("Excluir nao funfou");
					e1.printStackTrace();
				}
			}
		});

		this.btnProcuraId.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					TelaCadHardware.this.pId();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		this.tabela.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if (tabela.getSelectedRow() >= 0) {
					for (int i = 0; i < txtId.getItemCount(); i++) {
						txtId.setSelectedIndex(tabela.getSelectedRow());
						txtDescricao.setText("" + tabela.getValueAt(tabela.getSelectedRow(), 1));
						txtPrecoUnit.setText("" + tabela.getValueAt(tabela.getSelectedRow(), 2));
						txtQtdAtual.setText("" + tabela.getValueAt(tabela.getSelectedRow(), 3));
						txtQtdMinima.setText("" + tabela.getValueAt(tabela.getSelectedRow(), 4));
					}

					// tabela.getValueAt(tabela.getSelectedRow(), 0));
				}
			}
		});
	}

	@Override
	void limparFormulario() {
		System.out.println("void limparFormulario()");
		this.txtDescricao.setText("");
		this.txtPrecoUnit.setText("");
		this.txtQtdAtual.setText("");
		this.txtQtdMinima.setText("");
	}

	@Override
	void salvar() {
		if (!(txtDescricao.getText().isEmpty() || txtPrecoUnit.getText().isEmpty() || txtPrecoUnit.getText().isEmpty()
				|| txtQtdAtual.getText().isEmpty() || txtQtdMinima.getText().isEmpty())) {
			try {
				this.hardware.setDescricao(this.txtDescricao.getText());
				double PrecoUnitario = Double.parseDouble(this.txtPrecoUnit.getText());
				this.hardware.setPrecoUnitario(PrecoUnitario);
				int QtdAtual = Integer.parseInt(this.txtQtdAtual.getText());
				this.hardware.setQtdAtual(QtdAtual);
				int QtdMinima = Integer.parseInt(this.txtQtdMinima.getText());
				this.hardware.setQtdMinima(QtdMinima);
				Connection connection = br.edu.etec.persistence.JdbcUtil.getConnection();
				br.edu.etec.persistence.HardwareJdbcDAO hardwareJdbcDAO = new HardwareJdbcDAO(connection);
				hardwareJdbcDAO.salvar(this.hardware);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else JOptionPane.showMessageDialog(null, "Todos os campos tem que estar preenchidos!");
	}

	@Override
	void cancelar() {
		this.setVisible(false);
	}

	@Override
	void alterar() throws SQLException {
		String id = "" + this.txtId.getSelectedItem();
		if (!(txtDescricao.getText().isEmpty() || txtPrecoUnit.getText().isEmpty() || txtPrecoUnit.getText().isEmpty()
				|| txtQtdAtual.getText().isEmpty() || txtQtdMinima.getText().isEmpty())) {
			try {
				int idInt = Integer.parseInt(id);
				Connection conn = br.edu.etec.persistence.JdbcUtil.getConnection();
				HardwareJdbcDAO hardwareJdbcDAO = new HardwareJdbcDAO(conn);
				Hardware cli = hardwareJdbcDAO.findById(idInt);
				if (cli != null) {
					this.hardware.setId((Integer) this.txtId.getSelectedItem());
					this.hardware.setDescricao(this.txtDescricao.getText());
					this.hardware.setPrecoUnitario(Double.parseDouble(this.txtPrecoUnit.getText()));
					this.hardware.setQtdAtual(Integer.parseInt(this.txtQtdAtual.getText()));
					this.hardware.setQtdMinima(Integer.parseInt(this.txtQtdMinima.getText()));
					hardwareJdbcDAO.alterar(this.hardware);
				} else {
					JOptionPane.showMessageDialog(this, "Nao ha hardwares com esse id");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else JOptionPane.showMessageDialog(null, "Todos os campos tem que estar preenchidos!");
	}

	@Override
	void excluir() throws SQLException {
		String id = "" + this.txtId.getSelectedItem();
		try {
			int idInt = Integer.parseInt(id);
			Connection conn = br.edu.etec.persistence.JdbcUtil.getConnection();
			HardwareJdbcDAO hardwareJdbcDAO = new HardwareJdbcDAO(conn);
			hardwareJdbcDAO.excluir(idInt);
			this.limparFormulario();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void pId() throws SQLException {
		String id = "" + this.txtId.getSelectedItem();
		try {
			int idInt = Integer.parseInt(id);
			Connection conn = br.edu.etec.persistence.JdbcUtil.getConnection();
			HardwareJdbcDAO hardwareJdbcDAO = new HardwareJdbcDAO(conn);
			Hardware cc = hardwareJdbcDAO.findById(idInt);
			txtDescricao.setText(cc.getDescricao());
			txtPrecoUnit.setText("" + cc.getPrecoUnitario());
			txtQtdAtual.setText("" + cc.getQtdAtual());
			txtQtdMinima.setText("" + cc.getQtdMinima());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void listar() throws SQLException {
		this.modelo.setNumRows(0);
		Connection conn;
		try {
			conn = br.edu.etec.persistence.JdbcUtil.getConnection();
			HardwareJdbcDAO hardwareJdbcDAO = new HardwareJdbcDAO(conn);
			List<Hardware> list = hardwareJdbcDAO.listar();
			String[] strArr = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				this.modelo.addRow(new Object[] { list.get(i).getId(), list.get(i).getDescricao(),
						list.get(i).getQtdAtual(), list.get(i).getQtdMinima(), list.get(i).getPrecoUnitario() });
				/*
				 * int id = list.get(i).getId(); String descricao = list.get(i).getDescricao();
				 * strArr[i] = id + " - " + descricao;
				 */
			}
			TelaCadHardware.this.setarIds();
			this.list.setListData(strArr);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void setarIds() throws SQLException {
		Connection conn;
		try {
			conn = br.edu.etec.persistence.JdbcUtil.getConnection();
			HardwareJdbcDAO hardwareJdbcDAO = new HardwareJdbcDAO(conn);
			List<Id> list = hardwareJdbcDAO.listarIds();
			String[] strArr = new String[list.size()];
			this.txtId.setModel(new DefaultComboBoxModel());
			for (int i = 0; i < list.size(); i++) {
				this.txtId.addItem(list.get(i).getId());
				/*
				 * int id = list.get(i).getId(); String nome = list.get(i).getNome(); strArr[i]
				 * = id + " - " + nome;
				 */
			}
			this.list.setListData(strArr);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
