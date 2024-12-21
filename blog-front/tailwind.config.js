/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    screens: {
      '2xl': { 'max': '1920px' }, // 1919px 이하
      'xl': { 'max': '1440px' },  // 1440px 이하
      'lg': { 'max': '1200px' },
      'md': { 'max': '1056px' },  // 1056px 이하
      'sm': { 'max': '970px'}, //970px 이하
      'xs': { 'max': '768px'},
      '2xs': { 'max': '425px'},
    },
    extend: {
      boxShadow: {
        'custom-default': 'rgba(60, 64, 67, 0.3) 0px 1px 2px 0px, rgba(60, 64, 67, 0.15) 0px 2px 6px 2px',
        'header-dropdown-shadow': '0px 0px 10px -4px rgba(0,0,0,0.75)',
      },
      maxWidth: {
        '1728': '1728px', // 기본 max-width
        '1376': '1376px', // 2xl 일때
        '1024': '1024px', // xl 일때
        '768': '768px',
      }
    },
  },
  plugins: [
    require('@tailwindcss/line-clamp'),
  ],
}
