package br.edu.etec.view;

import java.awt.Dimension;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import br.edu.etec.model.Cliente;
import br.edu.etec.persistence.ClienteJdbcDAO;

import javax.swing.JScrollPane;

public class TelaCadClientes extends TelaDeCadastro {
	JList list = new JList();
	Cliente cliente = new Cliente();

	JLabel lbNome = new JLabel("Nome");
	JTextField txtNome = new JTextField();

	JLabel lbEndereco = new JLabel("Endereco");
	JTextField txtEndereco = new JTextField();

	JLabel lbFone = new JLabel("Fone");
	MaskFormatter mascaraFone = new MaskFormatter("(##) #####-####");
	JFormattedTextField txtFone = new JFormattedTextField(mascaraFone);

	JLabel lbEmail = new JLabel("Email");
	JTextField txtEmail = new JTextField();

	static String[] colunas={"id","nome","endereco","fone","email"};
	public TelaCadClientes() throws ParseException {
		super(4, 2, colunas); // quatro linhas e duas colunas Na Hora de add os componentes, considerar a
						// ordem deles Conforme usamos abaixo
		mascaraFone.setPlaceholderCharacter('_');
		
		this.painelParaCampos.add(lbNome);
		this.painelParaCampos.add(txtNome);

		this.painelParaCampos.add(lbEndereco);
		this.painelParaCampos.add(txtEndereco);

		this.painelParaCampos.add(lbFone);
		this.painelParaCampos.add(txtFone);

		this.painelParaCampos.add(lbEmail);
		this.painelParaCampos.add(txtEmail);
		System.out.println("Construtor TelaCadClientes()");

		this.btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TelaCadClientes.this.limparFormulario();
			}
		});
		this.btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					TelaCadClientes.this.salvar();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		this.btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TelaCadClientes.this.cancelar();
			}
		});

		this.btnAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					TelaCadClientes.this.alterar();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		this.btnListar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					TelaCadClientes.this.listar();
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
					TelaCadClientes.this.excluir();
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
					TelaCadClientes.this.pId();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	@Override
	void limparFormulario() {
		System.out.println("void limparFormulario()");
		this.txtNome.setText("");
		this.txtEmail.setText("");
		this.txtEndereco.setText("");
		this.txtFone.setText("");
	}

	@Override
	void salvar() {
		try {
			this.cliente.setNome(this.txtNome.getText());
			this.cliente.setEndereco(this.txtEndereco.getText());
			this.cliente.setFone(this.txtFone.getText());
			this.cliente.setEmail(this.txtEmail.getText());
			Connection connection = br.edu.etec.persistence.JdbcUtil.getConnection();
			br.edu.etec.persistence.ClienteJdbcDAO clienteJdbcDAO = new ClienteJdbcDAO(connection);
			clienteJdbcDAO.salvar(this.cliente);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	void cancelar() {
		this.setVisible(false);
	}

	@Override
	void alterar() throws SQLException {
		try {
			int idInt = Integer.parseInt(this.txtId.getText());
			Connection conn = br.edu.etec.persistence.JdbcUtil.getConnection();
			ClienteJdbcDAO clienteJdbcDAO = new ClienteJdbcDAO(conn);
			Cliente cli = clienteJdbcDAO.findById(idInt);
			if (cli != null) {
				this.txtNome.setText(cli.getNome());
				this.txtEndereco.setText(cli.getEndereco());
				this.txtFone.setText(cli.getFone());
				this.txtEmail.setText(cli.getEmail());
			} else {
				JOptionPane.showMessageDialog(this, "Nao ha clientes com esse id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	void excluir() throws SQLException {
		String id = this.txtId.getText();
		try {
			int idInt = Integer.parseInt(id);
			Connection conn = br.edu.etec.persistence.JdbcUtil.getConnection();
			ClienteJdbcDAO clienteJdbcDAO = new ClienteJdbcDAO(conn);
			clienteJdbcDAO.excluir(idInt);
			this.limparFormulario();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void pId() throws SQLException {
		String id = this.txtId.getText();
		try {
			int idInt = Integer.parseInt(id);
			Connection conn = br.edu.etec.persistence.JdbcUtil.getConnection();
			ClienteJdbcDAO clienteJdbcDAO = new ClienteJdbcDAO(conn);
			Cliente cc = clienteJdbcDAO.findById(idInt);
			txtEmail.setText(cc.getEmail());
			txtEndereco.setText(cc.getEndereco());
			txtFone.setText(cc.getFone());
			txtNome.setText(cc.getNome());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void listar() throws SQLException {
		this.modelo.setNumRows(0);
		System.out.println("roudou");
		Connection conn;
		try {
			conn = br.edu.etec.persistence.JdbcUtil.getConnection();
			ClienteJdbcDAO clienteJdbcDAO = new ClienteJdbcDAO(conn);
			List<Cliente> list = clienteJdbcDAO.listar();
			String[] strArr = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				this.modelo.addRow(new Object[] {list.get(i).getId(),list.get(i).getNome(),list.get(i).getEndereco(),list.get(i).getFone(),list.get(i).getEmail()});
				/*int id = list.get(i).getId();
				String nome = list.get(i).getNome();
				strArr[i] = id + " - " + nome;*/
			}
			this.list.setListData(strArr);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}