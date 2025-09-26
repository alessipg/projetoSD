package org.alessipg.client.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.alessipg.client.infra.tcp.TcpClient;
import org.alessipg.shared.enums.StatusTable;

public class MovieManager extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private List<Movie> movies;

    public MovieManager() {
        setTitle("Movie Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        movies = new ArrayList<>();
        tableModel = new DefaultTableModel(new Object[]{"Título", "Ano", "Diretor"}, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnCreate = new JButton("Criar");
        JButton btnEdit = new JButton("Editar");
        JButton btnDelete = new JButton("Excluir");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnCreate);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action: Create
        btnCreate.addActionListener(e -> openCreateDialog());

        // Action: Edit
        btnEdit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                openEditDialog(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um filme para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Action: Delete
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int option = JOptionPane.showConfirmDialog(this,
                        "Deseja excluir este filme?", "Confirmar Exclusão",
                        JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    movies.remove(selectedRow);
                    tableModel.removeRow(selectedRow);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um filme para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void openCreateDialog() {
        MovieDialog dialog = new MovieDialog(this, "Criar Filme", null);
        Movie movie = dialog.showDialog();
        if (movie != null) {
            movies.add(movie);
            tableModel.addRow(new Object[]{movie.getTitulo(), movie.getAno(), movie.getDiretor()});
        }
    }

    private void openEditDialog(int rowIndex) {
        Movie movie = movies.get(rowIndex);
        MovieDialog dialog = new MovieDialog(this, "Editar Filme", movie);
        Movie updated = dialog.showDialog();
        if (updated != null) {
            movies.set(rowIndex, updated);
            tableModel.setValueAt(updated.getTitulo(), rowIndex, 0);
            tableModel.setValueAt(updated.getAno(), rowIndex, 1);
            tableModel.setValueAt(updated.getDiretor(), rowIndex, 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MovieManager().setVisible(true);
        });
    }
}

// Movie model
class Movie {
    private String id;
    private String titulo;
    private String diretor;
    private String ano;
    private List<String> genero;
    private String sinopse;

    public Movie(String id, String titulo, String diretor, String ano, List<String> genero, String sinopse) {
        this.id = id;
        this.titulo = titulo;
        this.diretor = diretor;
        this.ano = ano;
        this.genero = genero;
        this.sinopse = sinopse;
    }

    // Getters
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDiretor() { return diretor; }
    public String getAno() { return ano; }
    public List<String> getGenero() { return genero; }
    public String getSinopse() { return sinopse; }

    // Setters
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDiretor(String diretor) { this.diretor = diretor; }
    public void setAno(String ano) { this.ano = ano; }
    public void setGenero(List<String> genero) { this.genero = genero; }
    public void setSinopse(String sinopse) { this.sinopse = sinopse; }
}

// Dialog for creating/editing movies
class MovieDialog extends JDialog {
    private JTextField txtTitulo;
    private JTextField txtAno;
    private JTextField txtDiretor;
    private JTextField txtGenero;
    private JTextArea txtSinopse;
    private Movie movie;
    private boolean confirmed = false;

    public MovieDialog(Frame parent, String title, Movie movie) {
        super(parent, title, true);
        this.movie = movie;

        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        formPanel.add(new JLabel("Título:"));
        txtTitulo = new JTextField(movie != null ? movie.getTitulo() : "");
        formPanel.add(txtTitulo);

        formPanel.add(new JLabel("Ano:"));
        txtAno = new JTextField(movie != null ? movie.getAno() : "");
        formPanel.add(txtAno);

        formPanel.add(new JLabel("Diretor:"));
        txtDiretor = new JTextField(movie != null ? movie.getDiretor() : "");
        formPanel.add(txtDiretor);

        formPanel.add(new JLabel("Gênero (separado por vírgula):"));
        txtGenero = new JTextField(movie != null ? String.join(", ", movie.getGenero()) : "");
        formPanel.add(txtGenero);

        formPanel.add(new JLabel("Sinopse:"));
        txtSinopse = new JTextArea(movie != null ? movie.getSinopse() : "");
        JScrollPane scrollSinopse = new JScrollPane(txtSinopse);

        add(formPanel, BorderLayout.NORTH);
        add(scrollSinopse, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton btnOk = new JButton("OK");
        JButton btnCancel = new JButton("Cancelar");
        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        btnOk.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this,
                    "Deseja confirmar as alterações?", "Confirmação",
                    JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                //StatusTable res = client.criarFilme(txtTitulo.getText(), txtDiretor.getText(), txtAno.getText(), List.of(txtGenero.getText().split(",")), txtSinopse.getText());
                dispose();
            }
        });

        btnCancel.addActionListener(e -> dispose());
    }

    public Movie showDialog() {
        setVisible(true);
        if (confirmed) {
            List<String> generos = new ArrayList<>();
            if (!txtGenero.getText().trim().isEmpty()) {
                for (String g : txtGenero.getText().split(",")) {
                    generos.add(g.trim());
                }
            }

            if (movie == null) {
                movie = new Movie(
                        String.valueOf(System.currentTimeMillis()),
                        txtTitulo.getText(),
                        txtDiretor.getText(),
                        txtAno.getText(),
                        generos,
                        txtSinopse.getText()
                );
            } else {
                movie.setTitulo(txtTitulo.getText());
                movie.setAno(txtAno.getText());
                movie.setDiretor(txtDiretor.getText());
                movie.setGenero(generos);
                movie.setSinopse(txtSinopse.getText());
            }
            return movie;
        }
        return null;
    }
    
}
