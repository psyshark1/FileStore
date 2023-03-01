package servlets;

import entity.FileDB;
import model.ModelFile;
import props.Props;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@WebServlet(name = "genericServlet")
public class genericServlet extends HttpServlet {

    public static final String[] bE  = {"wasd","wcnt","wdvs","wgsm","wkvk","wmgf","wsbr","wurl","wvlg"};

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String BE = request.getParameter("be");
        boolean BEcheck = false; boolean hasBEperm = false;
        StringBuilder from = new StringBuilder(request.getParameter("from"));
        StringBuilder to = new StringBuilder(request.getParameter("to"));
        String number = request.getParameter("number");

        if (!from.toString().equals("")){
            if (!isValidDate(from.toString().trim(),"dd.MM.yyyy")){
                errors(request,response,"Неверный формат Дата С");
                return;
            }
        }else {
            errors(request,response,"Дата С отсутствует");
            return;
        }

        if (!to.toString().equals("")) {
            if (!isValidDate(to.toString().trim(), "dd.MM.yyyy")) {
                errors(request, response, "Неверный формат Дата По");
                return;
            }
        }else{
            errors(request,response,"Дата По отсутствует");
            return;
        }

        if (!number.equals("")){
            try {
                Integer.parseInt(number.trim());
            }catch (NumberFormatException nfe){
                errors(request,response,"Номер контрагента имеет нечисловой формат");
                return;
            }
        }

        for (String eB: bE) {
            if (eB.equalsIgnoreCase(BE)) {
                BEcheck = true;
                hasBEperm = request.isUserInRole(BE);
                break;}
        }

        if (!BEcheck) {
            errors(request,response,"Неверный формат Балансовой едницы");
            return;
        }

        if (!hasBEperm) {
            errors(request,response,"Отсутствуют разрешения на загрузку актов по " + BE);
            return;
        }

        ModelFile mFile = null;
        try {
            mFile = new ModelFile();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        List<FileDB> files = null;
        try {
            assert mFile != null;
            files = mFile.fList(BE,number,from,to);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assert files != null;
        if (files.size() == 0) {
            errors(request,response,"По вашему запросу ничего не найдено");
            return;
        }

        Props props = Props.getInstance();
        StringBuilder temp_zipname = new StringBuilder(props.getTempFolder());
        temp_zipname.append(System.currentTimeMillis() / 100L).append(".zip");

        ZipOutputStream zipos = new ZipOutputStream(new FileOutputStream(temp_zipname.toString()));
        for (FileDB file : files){
            Blob blob =  file.getContent();
            InputStream is = null;
            try {
                is = blob.getBinaryStream();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            ZipEntry zentry = new ZipEntry(file.getFilename());
            zipos.putNextEntry(zentry);

            //byte[] bin = new byte[is.available()];
            //is.read(bin);
            //is.close();
            //zipos.write(bin);
            byte[] bin = new byte[1024];
            int binRead;
            while((binRead = is.read(bin)) >= 0) {
                zipos.write(bin, 0, binRead);
            }
            is.close();
            zipos.closeEntry();
        }

        zipos.flush();
        zipos.close();
        ReferenceQueue<ZipOutputStream> ref = new ReferenceQueue<>();
        PhantomReference<ZipOutputStream> PhRef = new PhantomReference<>(zipos,ref);
        zipos = null;
        PhRef.clear();

        FileInputStream zipis = new FileInputStream(temp_zipname.toString());
        int size = zipis.available();
        byte[] bin = new byte[size];
        zipis.read(bin);
        zipis.close();

        Files.delete(Paths.get(temp_zipname.toString()));

        temp_zipname.delete(0,temp_zipname.length());
        if (!number.equals("")){
            temp_zipname.append(number).append("_").append(BE).append("_").append(from).append("_").append(to).append(".zip");
        }else{
            temp_zipname.append(BE).append("_").append(from).append("_").append(to).append(".zip");
        }

        response.setHeader("Content-Length",String.valueOf(size));
        response.setHeader("Content-Disposition","attachment; filename=\"" + temp_zipname.toString() + "\"");
        String contentType = this.getServletContext().getMimeType(temp_zipname.toString());
        response.setHeader("Content-Type", contentType);
        response.getOutputStream().write(bin);
        response.getOutputStream().flush();
        response.getOutputStream().close();
        doGet(request, response);
    }

    private boolean isValidDate (String value, String datePattern){
        if (value == null || datePattern == null || datePattern.length() <= 0) {return false;}

        SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
        formatter.setLenient(false);
        try {
            formatter.parse(value);
        }
        catch (ParseException e){
            return false;
        }
        return true;
    }

    private void errors(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {

        request.setAttribute("err", errorMessage);
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pages/getActs.jsp");
        dispatcher.forward(request, response);

    }

/*    private StringBuilder replaceAll(StringBuilder builder, String from, String to) {
        int index = builder.indexOf(from);
        while (index != -1) {
            builder.replace(index, index + from.length(), to);
            index += to.length(); // Move to the end of the replacement
            index = builder.indexOf(from, index);
        }
        return builder;
    }*/

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        RequestDispatcher reqD = request.getRequestDispatcher("pages/getActs.jsp");
        reqD.forward(request, response);
    }
}
