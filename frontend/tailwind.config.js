module.exports = {
  content: ['./src/**/*.{js,jsx,ts,tsx,html}'],
  theme: {
    extend: {
      screens: {
        sm: '720px',
        md: '760px',
        lg: '1100px',
        xl: '1440px',
      },
    },
    container: false, // container 기본 비활성화
  },
  plugins: [],
};
