## BioPipeline Creator - A user-friendly Java-based GUI for managing and customizing biological data pipelines
<p align="justify"> BioPipeline Creator, a user-friendly Java-based GUI that allows different software tools to be integrated into the repertoire while ensuring easy user interaction via an accessible graphical interface. Consisting of client and server software components, BioPipeline Creator provides an intuitive graphical interface that simplifies the use of various bioinformatics tools for users without advanced computer skills. It can run on less sophisticated devices or workstations, allowing users to keep their operating system without having to switch to another compatible system. The server is responsible for the processing tasks and can perform the analysis in the user's local or remote network structure. Compatible with the most important operating systems<p>

### Technology
<image src="https://github.com/allanverasce/allanverasce/assets/25986290/e9eef5db-3d9e-419d-bc31-c29c16076146" alt="Image" width="50"/>
<image src="https://github.com/allanverasce/allanverasce/assets/25986290/3f178481-786d-4e6f-b46f-7e10732e9ca8" alt="Image" width="50"/>
<image src="https://github.com/allanverasce/allanverasce/assets/25986290/edfd02bc-1396-47a8-886f-c52d10508b0d" alt="Image" width="50"/>
<image src="https://github.com/allanverasce/bpc/assets/25986290/ec57d394-ea98-4755-b6ea-e95acec67aa0" alt="Image" width="50"/>
<image src="https://github.com/allanverasce/bpc/assets/25986290/0f2e354d-7a51-4b7a-b04a-fa8f9189d301" alt="Image" width="50"/>

### Quick Guide for the Impatient (Server and Client)
#### Server
* System Update
  <p>sudo apt update</p>
  <p>sudo apt upgrade</p>

* Docker installation
  <p>curl -fsSL https://get.docker.com -o get-docker.sh</p>
  <p>sudo sh get-docker.sh</p>

* Add your user to the "docker" group
  <p>sudo usermod -aG docker $USER</p>

* Create a Docker container
  <p>docker create -it --name pp2oa -p 16002:16002 biodufpa/pp2oa</p>

* Start container 
  <p>docker start -i pp2oa</p>

#### Client
* Running client
  <p>java -jar Client_PP2OA.jar</p>

### Video Tutorial 
You can download the Video [Tutorial](doc/BioPipelineCreator_Tutorial.mp4)

### See User Guide for details
You can download the [User Guide](doc/UserGuide.pdf)

### Screenshots
<img src="screenshots/AddTool.png" alt="AddTool" width="300" height="200" /> <img src="screenshots/CreateProject.png" alt="CreateProject" width="300" height="200" /> <img src="screenshots/MainWindow.png" alt="MainWindow" width="300" height="200" />

