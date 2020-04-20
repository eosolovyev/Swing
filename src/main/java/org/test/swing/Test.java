package org.test.swing;

import java.util.*;
import java.sql.*;
import javax.swing.*;
import java.awt.BorderLayout;
import javax.swing.table.*;

public class Test extends JFrame
{
    public Test() {
        try
        {
            // Устанавливаем менеджер расположения как в дотнете
            this.getContentPane().setLayout(new BorderLayout());
            // Создаем табличку и добавляем ее в центр окна
            JTable dbTable = new JTable();
            // JScrollPane нужна для отображения заголовка (ну и для скроллинга таблицы) -
            // в противном случае P_1 и P_2 в заголовке отображены не будут
            JScrollPane pane = new JScrollPane();
            pane.getViewport().add(dbTable);
            this.getContentPane().add(pane, BorderLayout.CENTER);

            // Получаю данные из БД
            Vector values = getDataFromDB();

            // "Шапка" - т.е. имена полей
            Vector header = new Vector();
            header.add("P_1");
            header.add("P_2");

            // Помещаю в модель таблицы данные
            DefaultTableModel dtm = (DefaultTableModel)dbTable.getModel();
            // Сначала данные, потом шапка
            dtm.setDataVector(values, header);
            // Ну все, теперь только размеры, видимость и чтобы по крестику закрывалось :-)
            this.setSize(640, 480);
            this.setVisible(true);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
    }
    /**
     * Возвращает набор значений в виде вектора - такими их умеет кушать модель
     * @return List список, состоящий из списков вида <P_1, P_2>
     * @throws Exception ибо лениво расписывать все.
     */
    public Vector getDataFromDB() throws Exception
    {
        // переменная под результат
        Vector result = new Vector();
        // Регистрируем драйвер в менеджере
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        // Подсоединяемся
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.10:1521:base1","scott", "1");
        Statement stmt = conn.createStatement();

        // Ну кто же так таблицы называет!!!
        String query = "SELECT P_1, P_2 FROM T_1";
        // Выполняем запрос, который у нас в переменной query
        ResultSet resultSet = stmt.executeQuery(query);

        // пока у нас есть данные - выполняем цикл
        String p1, p2;

        while(resultSet.next())
        {
            // Создаем новый список <P_1, P_2>
            Vector element = new Vector();

            // Первой колонкой у нас объявлен P_1
            p1 = resultSet.getString(1);
            // Второй - P_2
            p2 = resultSet.getString(2);
            // Добавляем по порядку
            element.add(p1);
            element.add(p2);

            // Присоединяем список к результату
            result.add(element);
        }
        // Освобождаем все ресурсы:
        resultSet.close();
        stmt.close();
        conn.close();

        return result;
    }
}