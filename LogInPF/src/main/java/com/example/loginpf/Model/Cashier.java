package com.example.loginpf.Model;

import com.example.loginpf.Repositories.UserRepository;

public class Cashier {

    public static class Result {
        public final boolean ok;
        public final String message;

        private Result(boolean ok, String message) {
            this.ok = ok;
            this.message = message;
        }
        public static Result ok(String msg) { return new Result(true, msg); }
        public static Result error(String msg) { return new Result(false, msg); }
    }


    public static Result deposit(BankAccount account, double amount) {
        if (account == null) return Result.error("Cuenta no válida");
        if (amount <= 0) return Result.error("El monto debe ser mayor a 0");
        account.setBalance(account.getBalance() + amount);
        return Result.ok(String.format("Depósito de $%.2f realizado. Nuevo saldo: $%.2f", amount, account.getBalance()));
    }

    public static Result withdraw(BankAccount account, double amount) {
        if (account == null) return Result.error("Cuenta no válida");
        if (amount <= 0) return Result.error("El monto debe ser mayor a 0");
        if (amount > account.getBalance()) return Result.error(String.format("Saldo insuficiente. Disponible: $%.2f", account.getBalance()));
        account.setBalance(account.getBalance() - amount);
        return Result.ok(String.format("Retiro de $%.2f realizado. Nuevo saldo: $%.2f", amount, account.getBalance()));
    }

    public static Result transfer(BankAccount fromAccount, String toAccount, double amount, String message) {
        if (fromAccount == null) return Result.error("Cuenta de origen no válida");
        if (toAccount == null || toAccount.isBlank()) return Result.error("La cuenta destino es obligatoria");
        if (amount <= 0) return Result.error("El monto debe ser mayor a 0");
        if (fromAccount.getNumber().equals(toAccount)) return Result.error("No puede transferir a la misma cuenta");

        BankAccount destAcc = UserRepository.buscarCuenta(toAccount);
        if (destAcc == null) return Result.error("La cuenta destino no existe");
        if (amount > fromAccount.getBalance()) return Result.error(String.format("Saldo insuficiente. Disponible: $%.2f", fromAccount.getBalance()));

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        destAcc.setBalance(destAcc.getBalance() + amount);

        Client fromOwner = UserRepository.buscarPropietario(fromAccount.getNumber());
        Client toOwner = UserRepository.buscarPropietario(toAccount);
        String msg = String.format(
                "Transferencia realizada con éxito.\nDe: %s (%s)\nA: %s (%s)\nMonto: $%.2f\nNuevo saldo: $%.2f%s",
                fromOwner != null ? fromOwner.getUsername() : "-", fromAccount.getNumber(),
                toOwner != null ? toOwner.getUsername() : "-", toAccount,
                amount, fromAccount.getBalance(),
                (message == null || message.isBlank() ? "" : "\nMensaje: " + message)
        );
        return Result.ok(msg);
    }

    // Legacy overloads (operate on primary account of the client)
    public static Result deposit(Client user, double amount) {
        if (user == null) return Result.error("Usuario no válido");
        return deposit(user.getPrimaryAccount(), amount);
    }

    public static Result withdraw(Client user, double amount) {
        if (user == null) return Result.error("Usuario no válido");
        return withdraw(user.getPrimaryAccount(), amount);
    }

    public static Result transfer(Client from, String toAccount, double amount, String message) {
        if (from == null) return Result.error("No hay un usuario en sesión");
        return transfer(from.getPrimaryAccount(), toAccount, amount, message);
    }
}
