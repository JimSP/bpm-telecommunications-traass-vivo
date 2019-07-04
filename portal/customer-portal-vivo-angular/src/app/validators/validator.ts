export class Validar {

    constructor() { }

    public static validarCPF(cpf) {
        console.log('CPF:', cpf);
        if (cpf.length !== 11 ||
            cpf === '00000000000' ||
            cpf === '11111111111' ||
            cpf === '22222222222' ||
            cpf === '33333333333' ||
            cpf === '44444444444' ||
            cpf === '55555555555' ||
            cpf === '66666666666' ||
            cpf === '77777777777' ||
            cpf === '88888888888' ||
            cpf === '99999999999') {
            return false;
        }
        let add = 0;
        for (let i = 0; i < 9; i++) {
            add += Number(cpf.charAt(i)) * (10 - i);
        }
        let rev = 11 - (add % 11);
        if (rev === 10 || rev === 11) {
            rev = 0;
        }
        if (rev !== Number(cpf.charAt(9))) {
            return false;
        }
        add = 0;
        for (let i = 0; i < 10; i++) {
            add += Number(cpf.charAt(i)) * (11 - i);
        }
        rev = 11 - (add % 11);
        if (rev === 10 || rev === 11) {
            rev = 0;
        }
        if (rev !== Number(cpf.charAt(10))) {
            return false;
        }
        return true;
    }

    public static validatorRg(numeroRG) {
        /*
        ##  Igor Carvalho de Escobar
        ##    www.webtutoriais.com
        ##   Java Script Developer
        */
        const numero = numeroRG.split('');
        const tamanho = numero.length;
        const vetor = new Array(tamanho);

        if (tamanho >= 1) {
            vetor[0] = Number(numero[0]) * 2;
        }
        if (tamanho >= 2) {
            vetor[1] = Number(numero[1]) * 3;
        }
        if (tamanho >= 3) {
            vetor[2] = Number(numero[2]) * 4;
        }
        if (tamanho >= 4) {
            vetor[3] = Number(numero[3]) * 5;
        }
        if (tamanho >= 5) {
            vetor[4] = Number(numero[4]) * 6;
        }
        if (tamanho >= 6) {
            vetor[5] = Number(numero[5]) * 7;
        }
        if (tamanho >= 7) {
            vetor[6] = Number(numero[6]) * 8;
        }
        if (tamanho >= 8) {
            vetor[7] = Number(numero[7]) * 9;
        }
        if (tamanho >= 9) {
            vetor[8] = Number(numero[8]) * 100;
        }

        let total = 0;

        if (tamanho >= 1) {
            total += vetor[0];
        }
        if (tamanho >= 2) {
            total += vetor[1];
        }
        if (tamanho >= 3) {
            total += vetor[2];
        }
        if (tamanho >= 4) {
            total += vetor[3];
        }
        if (tamanho >= 5) {
            total += vetor[4];
        }
        if (tamanho >= 6) {
            total += vetor[5];
        }
        if (tamanho >= 7) {
            total += vetor[6];
        }
        if (tamanho >= 8) {
            total += vetor[7];
        }
        if (tamanho >= 9) {
            total += vetor[8];
        }


        const resto = total % 11;
        if (resto !== 0) {
            return false;
        } else {
           return true;
        }
    }
}
