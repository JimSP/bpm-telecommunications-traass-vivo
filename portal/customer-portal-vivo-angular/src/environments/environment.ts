// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
  production: false,
  gatewayUrlSolicitation: 'http://bpm-hml.interfile.com.br/vt_solicitation',
  gatewayUrl: 'http://bpm-hml.interfile.com.br/vt_user',
  // gatewayUrlSolicitation: 'http://localhost:8092',
  // gatewayUrl: 'http://localhost:8091',
  version: '1.0.0.12',
  versionDate: '12/07/2018'
  // gatewayUrlSolicitation: 'http://192.168.206.55:8092',
  // gatewayUrl: 'http://192.168.206.55:8091',
};
/*
 * version 1.0.0.1 => Versão melhorias gerias: 02/07/2018
 * version 1.0.0.2 => Versão melhorias gerias: 03/07/2018 21:30
 * version 1.0.0.3 => Versão melhorias gerias: 04/07/2018 20:00
 * version 1.0.0.4 => Versão melhorias gerias: 05/07/2018 16:33
 * version 1.0.0.5 => Versão melhorias gerias: 06/07/2018 11:20
 * version 1.0.0.6 => Versão melhorias gerias: 06/07/2018 18:40
 * version 1.0.0.7 => Versão melhorias gerias: 11/07/2018 12:00
 * version 1.0.0.8 => Versão melhorias gerias: 11/07/2018 17:50
 * version 1.0.0.9 => Versão melhorias gerias: 12/07/2018 12:41
 * version 1.0.0.10 => Versão melhorias gerias: 12/07/2018 16:30
 * version 1.0.0.11 => Versão melhorias gerias: 12/07/2018 17:30
 * version 1.0.0.12 => Versão melhorias gerias: --/--/---- --:--
 * version -.-.-.-- => Versão melhorias gerias: --/--/---- --:--
 */
/*
 * In development mode, to ignore zone related error stack frames such as
 * `zone.run`, `zoneDelegate.invokeTask` for easier debugging, you can
 * import the following file, but please comment it out in production mode
 * because it will have performance impact when throw error
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
