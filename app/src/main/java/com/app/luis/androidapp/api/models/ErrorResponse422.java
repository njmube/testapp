package com.app.luis.androidapp.api.models;

/**
 * Created by Luis on 05/03/2016.
 */
public class ErrorResponse422 extends ErrorResponse {

    private Errors errors;

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    @Override
    public String getUserMessage() {
        return errors.getLastMessage();
    }

    public class Errors {
        private String[] nombre;
        private String[] apellido;
        private String[] fecha_nacimiento;
        private String[] email;
        private String[] sexo;
        private String[] password;

        public String[] getNombre() {
            return nombre;
        }

        public void setNombre(String[] nombre) {
            this.nombre = nombre;
        }

        public String[] getApellido() {
            return apellido;
        }

        public void setApellido(String[] apellido) {
            this.apellido = apellido;
        }

        public String[] getFecha_nacimiento() {
            return fecha_nacimiento;
        }

        public void setFecha_nacimiento(String[] fecha_nacimiento) {
            this.fecha_nacimiento = fecha_nacimiento;
        }

        public String[] getEmail() {
            return email;
        }

        public void setEmail(String[] email) {
            this.email = email;
        }

        public String[] getSexo() {
            return sexo;
        }

        public void setSexo(String[] sexo) {
            this.sexo = sexo;
        }

        public String[] getPassword() {
            return password;
        }

        public void setPassword(String[] password) {
            this.password = password;
        }

        public String getLastMessage() {
            if (nombre != null) {
                return getNombre()[0];
            }

            if (apellido != null) {
                return getApellido()[0];
            }

            if (fecha_nacimiento != null) {
                return getFecha_nacimiento()[0];
            }

            if (email != null) {
                return getEmail()[0];
            }

            if (sexo != null) {
                return getSexo()[0];
            }

            if (password != null) {
                return getPassword()[0];
            }

            return null;
        }
    }
}
