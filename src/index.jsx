import React from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter, Link } from "react-router-dom";
import { useMediaQuery } from "react-responsive";
// @ts-ignore
import './css/root.css';
import Footer from "./Footer.jsx";

function Root() {
    const isDesktop = useMediaQuery({ query: "(min-width: 801px)" });
    const width = isDesktop ? "50" : "80";

    return (
        <BrowserRouter>
            <div id="view">
                <h1>MenuMentor</h1>
                <div 
                    id="video-holder"
                    style={{
                        width: `${width}vw`,
                        height: `${(parseFloat(width) * 9) / 16}vw`
                    }}
                > 
                    <iframe 
                        src="https://drive.google.com/file/d/1t5o0a813PEGNkQIZODBMwaUzxciKhZDD/preview"
                        allow="autoplay; encrypted-media;"
                        allowFullScreen
                        width="100%"
                        height="100%"
                    >
                    </iframe>    
                </div>

                <h2>Introduction</h2>
                <p>
                    This is a cross-platform desktop application created using Java. I developed this project predominantly during my 11th and 12th grades in high school. 
                    The application's primary functionality is to generate weekly menus based on user preferences. Users start by inputting the minimum amount of calories 
                    they wish to consume daily. They can then add ingredients and courses to their preferences.
                </p>

                <p>
                    When adding a new course, users specify whether it is a first or second course and whether they prefer it during lunch or dinner. Additionally, the application 
                    prompts users for the maximum number of times they want to include a particular course in their weekly menu and the minimum wait time before repeating it. The 
                    system utilizes this information to generate a personalized weekly menu. If any of the specified conditions are not met, the affected course will be highlighted 
                    in red on the weekly menu.
                </p>

                <p>
                    In addition to menu generation, the application compiles a list of the required ingredients for the week and the necessary amounts. Users can print this list and 
                    save it on their computer. The application provides features such as modifying the proposed menu, obtaining caloric data for the week, generating a list of unmet 
                    requirements, and modifying all application data.
                </p>

                <h2>Installation Requirements</h2>
                <p>This application requires Java 17 and JVM 17 for proper functionality. To install Java 17 and JVM 17:</p>
                <ol>
                    <li>Visit the official Java website: <a href="https://www.oracle.com/java/technologies/downloads/#java17?er=221886" target="_blank" rel="noopener noreferrer">Java Downloads</a></li>
                    <li>Download and install Java 17 for your operating system.</li>
                    <li>Ensure that the installed Java version matches the application's requirements.</li>
                </ol>

                <h2>How to install</h2>
                <ol>
                    <li><a href="https://github.com/miiree12lb/MenuMentor/archive/main.zip" target="_blank" rel="noopener noreferrer">Download Zip</a></li>
                    <li>Unzip the folder in the desired folder on your computer.</li>
                    <li>Open the config.txt file and write the location on the computer where you want to save the SQLite database (Example on Windows: C:\Users\mirei\Desktop\MenuMentor\InternalDB.db).</li>
                    <li>Compile and run the code in a terminal.</li>
                </ol>
            </div>
            
            <div id="footer">
                <Footer />
            </div>
        </BrowserRouter>
    );
}

const rootElement = document.getElementById("root");
if (rootElement) {
    createRoot(rootElement).render(<Root />);
}

export default Root;
