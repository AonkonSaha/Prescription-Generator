import { useEffect, useState } from 'react';
import axios from 'axios';
import Navbar from '../navbar-footer/Navbar';

function CovidStats() {
  const [data, setData] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize] = useState(10);
  const baseURL = process.env.REACT_APP_BACK_END_BASE_URL || "http://localhost:8080";
  const token = localStorage.getItem('token');

  useEffect(() => {
    axios.get(`${baseURL}/api/third-party/v1/covid-stats`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    .then(response => setData(response.data))
    .catch(error => console.error('Error fetching data:', error));
  }, [baseURL, token]);

  const totalPages = Math.ceil(data.length / pageSize);
  const paginatedData = data.slice((currentPage - 1) * pageSize, currentPage * pageSize);

  const handlePrev = () => setCurrentPage(prev => Math.max(prev - 1, 1));
  const handleNext = () => setCurrentPage(prev => Math.min(prev + 1, totalPages));
  const handlePageClick = (page) => setCurrentPage(page);

  const renderPagination = () => {
    const pageButtons = [];

    if (totalPages <= 5) {
      for (let i = 1; i <= totalPages; i++) {
        pageButtons.push(i);
      }
    } else {
      if (currentPage > 2) pageButtons.push(1);
      if (currentPage > 3) pageButtons.push('...');
      for (let i = currentPage - 1; i <= currentPage + 1; i++) {
        if (i > 1 && i < totalPages) pageButtons.push(i);
      }
      if (currentPage < totalPages - 2) pageButtons.push('...');
      if (currentPage < totalPages - 1) pageButtons.push(totalPages);
    }

    return (
      <div style={styles.pagination}>
        <button onClick={handlePrev} disabled={currentPage === 1} style={styles.pageBtn}>
          &laquo;
        </button>

        {pageButtons.map((item, idx) => (
          <button
            key={idx}
            disabled={item === '...'}
            onClick={() => typeof item === 'number' && handlePageClick(item)}
            style={{
              ...styles.pageBtn,
              ...(item === currentPage ? styles.activePage : {}),
              ...(item === '...' ? styles.dots : {}),
            }}
          >
            {item}
          </button>
        ))}

        <button onClick={handleNext} disabled={currentPage === totalPages} style={styles.pageBtn}>
          &raquo;
        </button>
      </div>
    );
  };

  return (
    <>
      <Navbar />
      <div style={styles.wrapper}>
        <div style={styles.container}>
          <h2 style={styles.title}>COVID-19 Stats</h2>
          <table style={styles.table}>
            <thead>
              <tr style={styles.headerRow}>
                <th style={styles.th}>Country</th>
                <th style={styles.th}>Cases</th>
                <th style={styles.th}>Deaths</th>
                <th style={styles.th}>Recovered</th>
              </tr>
            </thead>
            <tbody>
              {paginatedData.map((country, idx) => (
                <tr key={idx} style={idx % 2 === 0 ? styles.evenRow : styles.oddRow}>
                  <td style={styles.td}>{country.country}</td>
                  <td style={styles.td}>{country.cases.toLocaleString()}</td>
                  <td style={styles.td}>{country.deaths.toLocaleString()}</td>
                  <td style={styles.td}>{country.recovered.toLocaleString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
          {renderPagination()}
        </div>
      </div>
    </>
  );
}

const styles = {
  wrapper: {
    display: 'flex',
    justifyContent: 'center',
    marginTop: '60px',
    padding: '0 20px',
  },
  container: {
    maxWidth: '1000px',
    width: '100%',
    fontFamily: 'Arial, sans-serif',
  },
  title: {
    textAlign: 'center',
    fontSize: '26px',
    marginBottom: '25px',
    color: '#333',
  },
  table: {
    width: '100%',
    borderCollapse: 'collapse',
    fontSize: '16px',
    backgroundColor: '#fff',
    boxShadow: '0 2px 5px rgba(0,0,0,0.1)',
  },
  headerRow: {
    backgroundColor: '#4CAF50',
    color: '#ffffff',
  },
  th: {
    padding: '14px 18px',
    border: '1px solid #ccc',
    textAlign: 'left',
  },
  td: {
    padding: '14px 18px',
    border: '1px solid #ddd',
  },
  evenRow: {
    backgroundColor: '#f9f9f9',
    transition: 'background 0.3s ease',
  },
  oddRow: {
    backgroundColor: '#ffffff',
    transition: 'background 0.3s ease',
  },
  pagination: {
    display: 'flex',
    justifyContent: 'center',
    marginTop: '30px',
    flexWrap: 'wrap',
    gap: '8px',
  },
  pageBtn: {
    padding: '8px 14px',
    fontSize: '14px',
    border: '1px solid #4CAF50',
    borderRadius: '20px',
    cursor: 'pointer',
    backgroundColor: '#fff',
    color: '#4CAF50',
    transition: 'all 0.3s ease',
  },
  activePage: {
    backgroundColor: '#4CAF50',
    color: '#fff',
    fontWeight: 'bold',
    transform: 'scale(1.05)',
  },
  dots: {
    cursor: 'default',
    backgroundColor: 'transparent',
    border: 'none',
    color: '#aaa',
    fontWeight: 'bold',
  }
};

export default CovidStats;
