using Microsoft.WindowsAPICodePack.Shell;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace ClassicalLauncer
{
	// https://web-dev.hatenablog.com/entry/csharp/notify-icon
	// https://qiita.com/entan05/items/c2f868ee292f3f1fd1ab
	// https://learn.microsoft.com/ja-jp/dotnet/desktop/winforms/advanced/how-to-extract-the-icon-associated-with-a-file-in-windows-forms?view=netframeworkdesktop-4.8

	// ショートカット関連
	// https://qiita.com/entan05/items/c2f868ee292f3f1fd1ab
	// https://mamesfactory.com/1119/

	static class Program
	{
		private static IWshRuntimeLibrary.WshShell wshShell;

		private enum FileTypes
		{
			DIRECTORY,
			NORMAL_FILE,
			EXE_FILE,
			SHORTCUT,
			URL_LINK,
		}

		/// <summary>
		/// アプリケーションのメイン エントリ ポイントです。
		/// </summary>
		[STAThread]
		static void Main()
		{
			wshShell = new IWshRuntimeLibrary.WshShell();

			Application.EnableVisualStyles();
			CreateNotifyIcon();
			Application.Run();
		}

		private static void CreateNotifyIcon()
		{
			// 常駐アプリ（タスクトレイのアイコン）を作成
			var icon = new NotifyIcon();
			icon.Icon = new Icon("Icon.ico");
			icon.ContextMenuStrip = ContextMenu();
			icon.Text = "Classical Launcher";
			icon.Visible = true;
		}

		private static ContextMenuStrip ContextMenu()
		{
			var menu = new MyContextMenuStrip();
			menu.AllowDrop = true;

			string[] cmds = System.Environment.GetCommandLineArgs();

			if (cmds.Length >= 2 && Directory.Exists(cmds[1])) {
				CreateTreeMenu(menu.Items, cmds[1]);
			} else {
#if DEBUG
				CreateTreeMenu(menu.Items, "C:\\User\\k-watanabe\\Documents\\@");
#endif
			}

			menu.Items.Add("-");
			menu.Items.Add("終了", null, (s, e) => {
				Application.Exit();
			});

//			menu.DefaultDropDownDirection = ToolStripDropDownDirection.AboveLeft;
//			menu.DefaultDropDownDirection = ToolStripDropDownDirection.BelowLeft;
//			menu.DefaultDropDownDirection = ToolStripDropDownDirection.Left;
//			menu.DefaultDropDownDirection = ToolStripDropDownDirection.Right;

			return menu;
		}

		private static ToolStripItemCollection CreateTreeMenu(ToolStripItemCollection items, string targetPath)
		{
			string[] paths;

			// フォルダ
			paths = Directory.GetDirectories(targetPath);
			foreach (string path in paths) {
				string file = Path.GetFileName(path);

				MyToolStripMenuItem item = new MyToolStripMenuItem(path, file);
				item.Image = new Icon("folderyellow_92963.ico").ToBitmap();

				CreateTreeMenu(item.DropDownItems, path);
				items.Add(item);
			}

			// ファイル
			paths = Directory.GetFiles(targetPath);
			foreach (string path in paths) {
				string file = Path.GetFileName(path);

				MyToolStripMenuItem item = new MyToolStripMenuItem(path, HideLinkAndUrlExtention(file), null, (s, e) => {
					Execute(path);
				});
				item.AllowDrop = true;
				item.DoubleClickEnabled = true;

				// ファイルアイコン
				Icon iconForFile = GetFileIcon(path);
				item.Image = iconForFile.ToBitmap();

				items.Add(item);
			}

			return items;
		}

		private static FileTypes GetFileTypes(string filePath)
		{
			if (!File.Exists(filePath)) {
				return FileTypes.NORMAL_FILE;
			}

			switch (Path.GetExtension(filePath).ToLower()) {
				case ".link":
				case ".lnk":
					return FileTypes.SHORTCUT;
				case ".url":
					return FileTypes.URL_LINK;
				case ".exe":
					return FileTypes.EXE_FILE;
			}

			return FileTypes.NORMAL_FILE;
		}

		private static string HideLinkAndUrlExtention(string filePath)
		{
			switch (Path.GetExtension(filePath).ToLower()) {
				case ".link":
				case ".lnk":
				case ".url":
					return filePath.Substring(0, filePath.Length - Path.GetExtension(filePath).Length);

				default:
					return filePath;
			}

		}

		private static Icon GetFileIcon(string filePath)
		{
			if (!File.Exists(filePath)) {
				return SystemIcons.WinLogo;
			}

			switch (GetFileTypes(filePath))
			{
				case FileTypes.SHORTCUT:
					// ショートカットオブジェクトの取得
					IWshRuntimeLibrary.IWshShortcut shortcut =
						(IWshRuntimeLibrary.IWshShortcut)wshShell.CreateShortcut(filePath);

					string targetPath = shortcut.TargetPath.ToString();
					// Debug.WriteLine("===========================");
					// Debug.WriteLine(filePath);
					// Debug.WriteLine(shortcut.FullName);
					// Debug.WriteLine(shortcut.ToString());
					// Debug.WriteLine(shortcut.WorkingDirectory);
					// Debug.WriteLine(targetPath);

					if (!File.Exists(targetPath)) {
						return SystemIcons.WinLogo;
					}

					// ショートカットのリンク先の取得
					return System.Drawing.Icon.ExtractAssociatedIcon(targetPath);

				case FileTypes.URL_LINK:
				default:
					return System.Drawing.Icon.ExtractAssociatedIcon(filePath);
			}
		}

		private static void Execute(string filePath)
		{
			switch (GetFileTypes(filePath)) {
				case FileTypes.SHORTCUT:
				case FileTypes.NORMAL_FILE:
				case FileTypes.EXE_FILE:
				case FileTypes.URL_LINK:
					if(File.Exists(filePath)) {
						Process.Start(filePath);
					} else {
						MessageBox.Show("ファイルがありません。", "エラー", MessageBoxButtons.OK, MessageBoxIcon.Error);
					}
					break;
			}
		}
	}

	class MyContextMenuStrip : ContextMenuStrip
	{
		protected override void OnDragDrop(System.Windows.Forms.DragEventArgs drgevent)
		{
			string a = "";
		}

		protected override void OnDragEnter(System.Windows.Forms.DragEventArgs drgevent)
		{
			string a = "";
		}

		protected override void OnDragLeave(EventArgs e)
		{
			string a = "";
		}

		protected override void OnDragOver(System.Windows.Forms.DragEventArgs drgevent)
		{
			string a = "";
		}
	}

	class MyToolStripMenuItem : ToolStripMenuItem
	{
		public MouseButtons LastMouseButtons { get; private set; }
		public int LastX { get; private set; }
		public int LastY { get; private set; }
		public string path { get; private set; }


		public MyToolStripMenuItem(string path, string text) :
			base(text)
		{
			this.path = path;
		}

		public MyToolStripMenuItem(string path, string text, Image image, EventHandler onClick) :
			base(text, image, onClick)
		{
			this.path = path;
		}

		protected override void OnDragDrop(System.Windows.Forms.DragEventArgs drgevent)
		{
			string a = "";
		}

		protected override void OnDragEnter(System.Windows.Forms.DragEventArgs drgevent)
		{
			string a = "";
		}

		protected override void OnDragLeave(EventArgs e)
		{
			string a = "";
		}

		protected override void OnDragOver(System.Windows.Forms.DragEventArgs drgevent)
		{
			string a = "";
		}
		
		protected override void OnDoubleClick(EventArgs e)
		{
			string a = "";
		}
		protected override void OnMouseDown(System.Windows.Forms.MouseEventArgs e)
		{
			string a = "";

		}
		protected override void OnMouseUp(System.Windows.Forms.MouseEventArgs e)
		{
			this.LastMouseButtons = e.Button;
			this.LastX = e.X;
			this.LastY = e.Y;

			if (this.LastMouseButtons == MouseButtons.Right) {
				System.Diagnostics.Process.Start(
					new System.Diagnostics.ProcessStartInfo {
						FileName = "explorer", //フルパスで指定せず「explorer」とだけ書く
						Arguments = @"/select," + path, //引数に「/select,」を付ける
						UseShellExecute = true,
						Verb = "open"
					}
				);
			}

		}
	}
}
