module.exports = {
  root: true,
  env: {
    node: true
  },
  extends: [
    'plugin:vue/essential',
    'eslint:recommended',
    '@vue/typescript/recommended',
    '@vue/prettier',
    '@vue/prettier/@typescript-eslint',
    'prettier',
    'prettier/vue'
  ],
  parserOptions: {
    ecmaVersion: 2020
  },
  rules: {
    //    'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'space-before-function-paren': ['error', 'never'],
    'no-new': 0,
    '@typescript-eslint/no-non-null-assertion': 0,
    '@typescript-eslint/no-explicit-any': 0,
    'prettier/prettier': [
      'warn',
      {
        indent_style: 'space',
        indent_size: 2,
        trim_trailing_whitespace: true,
        insert_final_newline: true,
        singleQuote: true,
        semi: false,
        printWidth: 100
      }
    ]
  }
}
