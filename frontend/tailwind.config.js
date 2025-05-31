module.exports = {
  content: ['./src/**/*.{js,jsx,ts,tsx,html}'],
  theme: {
    extend: {
      screens: {
        mb: { max: '639px' },
        sm: '640px',
        md: '768px',
        lg: '1024px',
        xl: '1280px',
      },
    },
    container: false, // container 기본 비활성화
  },
  plugins: [],
};
