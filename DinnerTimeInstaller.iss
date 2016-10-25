;DinnerTime installer script

[Setup]
AppName=DinnerTime
AppVersion=1.3
DefaultDirName={pf}\DinnerTime
DefaultGroupName=DinnerTime       
Compression=lzma2
SolidCompression=yes
OutputBaseFilename=DinnerTimeSetup
OutputDir=SETUP

UninstallDisplayIcon={app}\DinnerTime
UninstallDisplayName=DinnerTime
SetupIconFile=data\img\DinnerTimeIcon_Files.ico

[Tasks]
Name: desktopicon; Description: {cm:CreateDesktopIcon}; GroupDescription: {cm:AdditionalIcons}; Flags: unchecked
Name: StartMenuEntry; Description: "Démarrer l'application au démarrage" ; GroupDescription: "Windows Startup"; Flags: unchecked   

[Dirs]
Name: "{app}\data"
Name: "{app}\data\img"
Name: "{app}\data\sounds"
Name: "{app}\data\doc"
Name: "{app}\libs"

[Files]
Source: "DinnerTimeClient.jar"; DestDir: "{app}"
Source: "LICENSE"; DestDir: "{app}";
Source: "libs\swingx.jar"; DestDir: "{app}\libs"
Source: "data\img\*"; DestDir: "{app}\data\img"
Source: "data\doc\*"; DestDir: "{app}\data\doc"
Source: "data\sounds\*"; DestDir: "{app}\data\sounds"

[Icons]
Name: "{group}\DinnerTime"; Filename: "{app}\DinnerTimeClient.jar"; IconFilename: "{app}\data\img\DinnerTimeIcon_Files.ico"
Name: "{commondesktop}\DinnerTime"; Filename: "{app}\DinnerTimeClient.jar"; IconFilename: "{app}\data\img\DinnerTimeIcon_Files.ico"; Tasks: desktopicon

Name: "{userstartup}\DinnerTime"; Filename: "{app}\DinnerTimeClient.jar"; IconFilename: "{app}\data\img\DinnerTimeIcon_Files.ico"; Tasks:StartMenuEntry;